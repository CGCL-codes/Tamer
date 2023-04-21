package de.objectcode.openk.soa.connectors.sugarcrm.smooks;

import java.util.List;
import org.milyn.container.ExecutionContext;
import org.milyn.javabean.BeanAccessor;
import org.milyn.javabean.DataDecodeException;
import org.milyn.javabean.DataDecoder;
import org.w3c.dom.Element;
import com.sugarcrm.www.sugarcrm.Name_value;
import de.objectcode.openk.soa.connectors.sugarcrm.SugarEvent;
import de.objectcode.openk.soa.connectors.sugarcrm.SugarModule;
import de.objectcode.openk.soa.model.v1.EventType;
import de.objectcode.openk.soa.model.v1.ResponseType;
import de.objectcode.soa.common.mfm.api.accessor.IDataAccessor;
import de.objectcode.soa.common.mfm.api.normalize.NormalizedData;

public class RefValueBinding implements IValueBinding {

    String[] propertyPath;

    String refId;

    DataDecoder decoder;

    String typeAlias;

    String value;

    public RefValueBinding(String property, String refId) throws Exception {
        this.propertyPath = property.split("\\.");
        this.refId = refId;
        this.decoder = null;
        this.typeAlias = "String";
    }

    public void applyValue(NormalizedData normalizedData, Element element, ExecutionContext context) throws Exception {
        Object refObject = BeanAccessor.getBean(refId, context);
        if (refObject == null || !(refObject instanceof NormalizedData)) {
            return;
        }
        for (int i = 0; i < propertyPath.length; i++) {
            String property = propertyPath[i];
            int offset = property.indexOf('[');
            if (offset > 0) {
                int end = property.indexOf(']', offset + 1);
                int index = Integer.parseInt(property.substring(offset + 1, end));
                property = property.substring(0, offset);
                List<IDataAccessor> array = normalizedData.getComponentArray(property);
                if (array == null || array.size() <= index) {
                    normalizedData = (NormalizedData) normalizedData.addToComponentArray(property, index);
                } else {
                    normalizedData = (NormalizedData) array.get(index);
                }
            } else {
                NormalizedData component = (NormalizedData) normalizedData.getComponent(propertyPath[i]);
                if (component == null) {
                    component = (NormalizedData) normalizedData.addComponent(propertyPath[i]);
                }
                normalizedData = component;
            }
        }
        ((NormalizedData) refObject).marshal(normalizedData);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void applyValue(SugarModule module, Element element, ExecutionContext context) throws Exception {
        Object refObject = BeanAccessor.getBean(refId, context);
        if (null == refObject || !(refObject instanceof List<?>)) {
            System.out.println(" no List ");
            return;
        }
        if (decoder == null) {
            decoder = getDecoder(context);
        }
        module.setModuleNvl((List<Name_value>) refObject);
    }

    @Override
    public void applyValue(SugarEvent sugarEvent, Element element, ExecutionContext context) throws Exception {
        Object refObject = BeanAccessor.getBean(refId, context);
        if (refObject == null) {
            return;
        }
        if (refObject instanceof SugarModule) {
            sugarEvent.setModule(((SugarModule) refObject));
        }
        if (refObject instanceof NormalizedData) {
            NormalizedData nd;
            for (String p : propertyPath) {
                nd = new NormalizedData();
                if (p.equalsIgnoreCase("event")) {
                    nd.addValue("event", refObject);
                    EventType et = new EventType();
                    et.unmarshal(nd.getComponent("event"));
                    sugarEvent.setEvent(et);
                }
                if (p.equalsIgnoreCase("response")) {
                    nd.addValue("response", refObject);
                    ResponseType rt = new ResponseType();
                    rt.unmarshal(nd.getComponent("response"));
                    sugarEvent.setResponse(rt);
                }
            }
        }
    }

    @Override
    public void applyValue(List<Name_value> nameValueList, Element element, ExecutionContext context) throws Exception {
    }

    private DataDecoder getDecoder(ExecutionContext executionContext) throws DataDecodeException {
        List<?> decoders = executionContext.getDeliveryConfig().getObjects("decoder:" + typeAlias);
        if (decoders == null || decoders.isEmpty()) {
            decoder = DataDecoder.Factory.create(typeAlias);
        } else if (!(decoders.get(0) instanceof DataDecoder)) {
            throw new DataDecodeException("Configured decoder '" + typeAlias + ":" + decoders.get(0).getClass().getName() + "' is not an instance of " + DataDecoder.class.getName());
        } else {
            decoder = (DataDecoder) decoders.get(0);
        }
        return decoder;
    }
}
