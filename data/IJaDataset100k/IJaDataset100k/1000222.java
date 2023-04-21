package net.sf.istcontract.wsimport.client.sei;

import com.sun.xml.bind.api.AccessorException;
import com.sun.xml.bind.api.Bridge;
import com.sun.xml.bind.api.CompositeStructure;
import com.sun.xml.bind.api.RawAccessor;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.ws.Holder;
import javax.xml.ws.WebServiceException;
import net.sf.istcontract.wsimport.api.SOAPVersion;
import net.sf.istcontract.wsimport.api.message.Message;
import net.sf.istcontract.wsimport.api.message.Messages;
import net.sf.istcontract.wsimport.message.jaxb.JAXBMessage;
import net.sf.istcontract.wsimport.model.ParameterImpl;
import net.sf.istcontract.wsimport.model.WrapperParameter;
import java.util.List;

/**
 * Builds a JAXB object that represents the payload.
 *
 * @see MessageFiller
 * @author Kohsuke Kawaguchi
 */
abstract class BodyBuilder {

    abstract Message createMessage(Object[] methodArgs);

    static final BodyBuilder EMPTY_SOAP11 = new Empty(SOAPVersion.SOAP_11);

    static final BodyBuilder EMPTY_SOAP12 = new Empty(SOAPVersion.SOAP_12);

    private static final class Empty extends BodyBuilder {

        private final SOAPVersion soapVersion;

        public Empty(SOAPVersion soapVersion) {
            this.soapVersion = soapVersion;
        }

        Message createMessage(Object[] methodArgs) {
            return Messages.createEmpty(soapVersion);
        }
    }

    /**
     * Base class for those {@link BodyBuilder}s that build a {@link Message}
     * from JAXB objects.
     */
    private abstract static class JAXB extends BodyBuilder {

        /**
         * This object determines the binding of the object returned
         * from {@link #build(Object[])}.
         */
        private final Bridge bridge;

        private final SOAPVersion soapVersion;

        protected JAXB(Bridge bridge, SOAPVersion soapVersion) {
            assert bridge != null;
            this.bridge = bridge;
            this.soapVersion = soapVersion;
        }

        final Message createMessage(Object[] methodArgs) {
            return JAXBMessage.create(bridge, build(methodArgs), soapVersion);
        }

        /**
         * Builds a JAXB object that becomes the payload.
         */
        abstract Object build(Object[] methodArgs);
    }

    /**
     * Used to create a payload JAXB object just by taking
     * one of the parameters.
     */
    static final class Bare extends JAXB {

        /**
         * The index of the method invocation parameters that goes into the payload.
         */
        private final int methodPos;

        private final ValueGetter getter;

        /**
         * Creates a {@link BodyBuilder} from a bare parameter.
         */
        Bare(ParameterImpl p, SOAPVersion soapVersion, ValueGetter getter) {
            super(p.getBridge(), soapVersion);
            this.methodPos = p.getIndex();
            this.getter = getter;
        }

        /**
         * Picks up an object from the method arguments and uses it.
         */
        Object build(Object[] methodArgs) {
            return getter.get(methodArgs[methodPos]);
        }
    }

    /**
     * Used to handle a 'wrapper' style request.
     * Common part of rpc/lit and doc/lit.
     */
    abstract static class Wrapped extends JAXB {

        /**
         * Where in the method argument list do they come from?
         */
        protected final int[] indices;

        /**
         * Abstracts away the {@link Holder} handling when touching method arguments.
         */
        protected final ValueGetter[] getters;

        protected Wrapped(WrapperParameter wp, SOAPVersion soapVersion, ValueGetterFactory getter) {
            super(wp.getBridge(), soapVersion);
            List<ParameterImpl> children = wp.getWrapperChildren();
            indices = new int[children.size()];
            getters = new ValueGetter[children.size()];
            for (int i = 0; i < indices.length; i++) {
                ParameterImpl p = children.get(i);
                indices[i] = p.getIndex();
                getters[i] = getter.get(p);
            }
        }
    }

    /**
     * Used to create a payload JAXB object by wrapping
     * multiple parameters into one "wrapper bean".
     */
    static final class DocLit extends Wrapped {

        /**
         * How does each wrapped parameter binds to XML?
         */
        private final RawAccessor[] accessors;

        /**
         * Wrapper bean.
         */
        private final Class wrapper;

        /**
         * Creates a {@link BodyBuilder} from a {@link WrapperParameter}.
         */
        DocLit(WrapperParameter wp, SOAPVersion soapVersion, ValueGetterFactory getter) {
            super(wp, soapVersion, getter);
            wrapper = (Class) wp.getBridge().getTypeReference().type;
            List<ParameterImpl> children = wp.getWrapperChildren();
            accessors = new RawAccessor[children.size()];
            for (int i = 0; i < accessors.length; i++) {
                ParameterImpl p = children.get(i);
                QName name = p.getName();
                try {
                    accessors[i] = p.getOwner().getJAXBContext().getElementPropertyAccessor(wrapper, name.getNamespaceURI(), name.getLocalPart());
                } catch (JAXBException e) {
                    throw new WebServiceException(wrapper + " do not have a property of the name " + name, e);
                }
            }
        }

        /**
         * Packs a bunch of arguments into a {@link CompositeStructure}.
         */
        Object build(Object[] methodArgs) {
            try {
                Object bean = wrapper.newInstance();
                for (int i = indices.length - 1; i >= 0; i--) {
                    accessors[i].set(bean, getters[i].get(methodArgs[indices[i]]));
                }
                return bean;
            } catch (InstantiationException e) {
                Error x = new InstantiationError(e.getMessage());
                x.initCause(e);
                throw x;
            } catch (IllegalAccessException e) {
                Error x = new IllegalAccessError(e.getMessage());
                x.initCause(e);
                throw x;
            } catch (AccessorException e) {
                throw new WebServiceException(e);
            }
        }
    }

    /**
     * Used to create a payload JAXB object by wrapping
     * multiple parameters into a {@link CompositeStructure}.
     *
     * <p>
     * This is used for rpc/lit, as we don't have a wrapper bean for it.
     * (TODO: Why don't we have a wrapper bean for this, when doc/lit does!?)
     */
    static final class RpcLit extends Wrapped {

        /**
         * How does each wrapped parameter binds to XML?
         */
        private final Bridge[] parameterBridges;

        /**
         * List of Parameters packed in the body.
         * Only used for error diagnostics.
         */
        private final List<ParameterImpl> children;

        /**
         * Creates a {@link BodyBuilder} from a {@link WrapperParameter}.
         */
        RpcLit(WrapperParameter wp, SOAPVersion soapVersion, ValueGetterFactory getter) {
            super(wp, soapVersion, getter);
            assert wp.getTypeReference().type == CompositeStructure.class;
            this.children = wp.getWrapperChildren();
            parameterBridges = new Bridge[children.size()];
            for (int i = 0; i < parameterBridges.length; i++) parameterBridges[i] = children.get(i).getBridge();
        }

        /**
         * Packs a bunch of arguments intoa {@link CompositeStructure}.
         */
        CompositeStructure build(Object[] methodArgs) {
            CompositeStructure cs = new CompositeStructure();
            cs.bridges = parameterBridges;
            cs.values = new Object[parameterBridges.length];
            for (int i = indices.length - 1; i >= 0; i--) {
                Object arg = getters[i].get(methodArgs[indices[i]]);
                if (arg == null) {
                    throw new WebServiceException("Method Parameter: " + children.get(i).getName() + " cannot be null. This is BP 1.1 R2211 violation.");
                }
                cs.values[i] = arg;
            }
            return cs;
        }
    }
}
