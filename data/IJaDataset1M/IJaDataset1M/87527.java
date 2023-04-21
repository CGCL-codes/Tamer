package org.apache.myfaces.trinidadinternal.uinode;

import java.io.IOException;
import java.util.Iterator;
import javax.faces.component.UIComponent;
import java.lang.reflect.UndeclaredThrowableException;
import org.apache.myfaces.trinidad.logging.TrinidadLogger;
import org.apache.myfaces.trinidadinternal.ui.AttributeKey;
import org.apache.myfaces.trinidadinternal.ui.NodeRole;
import org.apache.myfaces.trinidadinternal.ui.Renderer;
import org.apache.myfaces.trinidadinternal.ui.RendererFactory;
import org.apache.myfaces.trinidadinternal.ui.RendererManager;
import org.apache.myfaces.trinidadinternal.ui.UIXRenderingContext;
import org.apache.myfaces.trinidadinternal.ui.RoledRenderer;
import org.apache.myfaces.trinidadinternal.ui.UIConstants;
import org.apache.myfaces.trinidadinternal.ui.UINode;
import org.apache.myfaces.trinidadinternal.ui.collection.AttributeMap;
import org.apache.myfaces.trinidadinternal.ui.data.BoundValue;
import org.apache.myfaces.trinidadinternal.ui.laf.base.PreAndPostRenderer;
import org.apache.myfaces.trinidad.component.UIXComponent;

/**
 * Subclass for UIX components.
 * <p>
 * @version $Name:  $ ($Revision: 245 $) $Date: 2008-11-25 19:05:42 -0500 (Tue, 25 Nov 2008) $
 * @deprecated This class comes from the old Java 1.2 UIX codebase and should not be used anymore.
 */
@Deprecated
public class UIXComponentUINode extends UIComponentUINode {

    public UIXComponentUINode(UIXComponent component, String namespace, AttributeMap attributes) {
        super(component, namespace);
        _attributes = attributes;
    }

    /**
   * Returns the role that this node occupies.
   */
    @Override
    public NodeRole getNodeRole(UIXRenderingContext context) {
        if (context != null) {
            Renderer renderer = getRenderer(context, this);
            if (renderer instanceof RoledRenderer) {
                return ((RoledRenderer) renderer).getNodeRole(context, this);
            } else if (renderer == null) {
                return UIConstants.STATE_ROLE;
            }
        }
        return UIConstants.UNKNOWN_ROLE;
    }

    @Override
    public Iterator<AttributeKey> getAttributeNames(UIXRenderingContext context) {
        AttributeMap attributes = getAttributeMap(false);
        if (attributes != null) {
            return attributes.attributeKeys(context);
        } else {
            return null;
        }
    }

    @Override
    public void setAttributeValue(AttributeKey attrKey, Object value) {
        AttributeMap attrDict = getAttributeMap((value != null));
        if (attrDict != null) {
            attrDict.setAttribute(attrKey, value);
        }
    }

    /**
   * Returns the value of the attribute with the specified name in the
   * RenderingContext.  If no attribute with the specified name exists
   * in this UINode, or a checked Exception occurs in retrieving the value of
   * the attribute, <CODE>null</CODE> will be returned.
   * <p>
   * Note that as with indexed children and named children, the presence of
   * of an attribute is no guarantee that the Renderer used to render this
   * UINode will actually use the attribute.  The presence of attributes
   * should only be considered as hints to the Renderer.
   * <p>
   * <STRONG>If the UINode is mutable and may be modified and read in different
   * threads, it is the programmer's responsibility to ensure proper
   * synchronization.
   * </STRONG>
   * <p>
   * @see #getAttributeNames
   */
    @Override
    public Object getAttributeValue(UIXRenderingContext context, AttributeKey attrKey) {
        return getAttributeValueImpl(context, attrKey, true);
    }

    /**
   * Returns the value of the attribute with a specified name, without
   * attempting to further resolve that value - as if , for instance,
   * it might be a BoundValue.
   * <p>
   * @see org.apache.myfaces.trinidadinternal.ui.data.BoundValue
   */
    @Override
    public Object getRawAttributeValue(UIXRenderingContext context, AttributeKey attrKey) {
        return getAttributeValueImpl(context, attrKey, false);
    }

    public void renderInternal(UIXRenderingContext context, UINode dataNode) throws IOException {
        Renderer renderer = null;
        try {
            renderer = getRenderer(context, dataNode);
        } catch (UndeclaredThrowableException e) {
            if (_LOG.isWarning()) _LOG.warning(e.getMessage());
            return;
        }
        if (renderer != null) {
            boolean pushAndPop = (context.getRenderedAncestorNode(0) != dataNode);
            if (pushAndPop) {
                context.pushChild(dataNode, null, -1);
                context.pushRenderedChild(context, dataNode);
            }
            try {
                renderer.render(context, dataNode);
            } catch (RuntimeException re) {
                _handleRenderException(re);
            } finally {
                if (pushAndPop) {
                    context.popRenderedChild(context);
                    context.popChild();
                }
            }
        } else {
            _logNoRenderer(context);
        }
    }

    public void prerenderInternal(UIXRenderingContext context, UINode dataNode) throws IOException {
        Renderer renderer = null;
        try {
            renderer = getRenderer(context, dataNode);
        } catch (UndeclaredThrowableException e) {
            if (_LOG.isWarning()) _LOG.warning(e.getMessage());
            return;
        }
        assert (renderer instanceof PreAndPostRenderer);
        if (renderer != null) {
            boolean pushAndPop = (context.getRenderedAncestorNode(0) != dataNode);
            if (pushAndPop) {
                context.pushChild(dataNode, null, -1);
                context.pushRenderedChild(context, dataNode);
            }
            try {
                ((PreAndPostRenderer) renderer).prerender(context, dataNode);
            } catch (RuntimeException re) {
                _handleRenderException(re);
            } finally {
                if (pushAndPop) {
                    context.popRenderedChild(context);
                    context.popChild();
                }
            }
        } else {
            _logNoRenderer(context);
        }
    }

    public void postrenderInternal(UIXRenderingContext context, UINode dataNode) throws IOException {
        Renderer renderer = null;
        try {
            renderer = getRenderer(context, dataNode);
        } catch (UndeclaredThrowableException e) {
            if (_LOG.isWarning()) _LOG.warning(e.getMessage());
            return;
        }
        assert (renderer instanceof PreAndPostRenderer);
        if (renderer != null) {
            boolean pushAndPop = (context.getRenderedAncestorNode(0) != dataNode);
            if (pushAndPop) {
                context.pushChild(dataNode, null, -1);
                context.pushRenderedChild(context, dataNode);
            }
            try {
                ((PreAndPostRenderer) renderer).postrender(context, dataNode);
            } catch (RuntimeException re) {
                _handleRenderException(re);
            } finally {
                if (pushAndPop) {
                    context.popRenderedChild(context);
                    context.popChild();
                }
            }
        } else {
            _logNoRenderer(context);
        }
    }

    private void _handleRenderException(RuntimeException re) throws IOException {
        if (re instanceof UndeclaredThrowableException) {
            Throwable rootCause = ((UndeclaredThrowableException) re).getCause();
            if (rootCause instanceof IOException) throw ((IOException) rootCause);
        }
        throw re;
    }

    private void _logNoRenderer(UIXRenderingContext context) {
        if (_LOG.isWarning()) {
            RendererManager manager = context.getRendererManager();
            RendererFactory factory = manager.getFactory(getNamespaceURI());
            if (factory == null) {
                _LOG.warning("NO_RENDERERFACTORY_REGISTERED_COMPONENT", getNamespaceURI());
            } else {
                _LOG.warning("NO_RENDERER_REGISTERED", this);
            }
        }
    }

    protected Renderer getRenderer(UIXRenderingContext context, UINode dataNode) {
        RendererManager manager = context.getRendererManager();
        String localName = getLocalName();
        if (localName == null) return null;
        return manager.getRenderer(getNamespaceURI(), localName);
    }

    /**
   * Bottleneck method for all attribute getting.
   */
    protected Object getAttributeValueImpl(UIXRenderingContext context, AttributeKey attrKey, boolean returnBoundValue) {
        AttributeMap attributes = getAttributeMap(false);
        if (attributes != null) {
            Object value = attributes.getAttribute(context, attrKey);
            if (returnBoundValue && (value instanceof BoundValue)) {
                value = ((BoundValue) value).getValue(context);
            }
            return value;
        } else {
            return null;
        }
    }

    /**
   * Returns the AttributeMap used to store attributes.
   * <p>
   * @param createIfNull if true,  creates
   *   an AttributeMap object if one has not yet been created.
   */
    protected final AttributeMap getAttributeMap(boolean createIfNull) {
        if (createIfNull && (_attributes == null)) {
            throw new IllegalStateException();
        }
        return _attributes;
    }

    static UIXComponentUINode __getAdapter(UIComponent component) {
        return (UIXComponentUINode) __getUINode(component);
    }

    private AttributeMap _attributes;

    private static final TrinidadLogger _LOG = TrinidadLogger.createTrinidadLogger(UIXComponentUINode.class);
}
