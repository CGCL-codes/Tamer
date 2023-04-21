package org.josef.web.jsf.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import org.josef.util.CDebug;
import org.josef.util.CString;

/**
 * Java Server Faces Utility class focusing on the UI Component Tree.
 * @author Kees Schotanus
 * @version 1.0 $Revision: 3201 $
 */
public final class JsfTreeUtil {

    /**
     * Private constructor prevents creation of an instance outside this class.
     */
    private JsfTreeUtil() {
    }

    /**
     * Gets the client id of the form in which the supplied uiComponent is
     * nested.
     * @param facesContext The FacesContext.
     * @param uiComponent Component to get the form id for.
     * @return The client id of the form in which the supplied uiComponent is
     *  nested or null when the supplied uiComponent is not nested within a
     *  form.
     *  <br/> This may be the case when the uiComponent is an output component.
     * @throws NullPointerException When the supplied uiComponent is null.
     */
    public static String getClientIdOfForm(final FacesContext facesContext, final UIComponent uiComponent) {
        CDebug.checkParameterNotNull(uiComponent, "uiComponent");
        UIForm form = getParent(uiComponent, UIForm.class);
        return form == null ? null : form.getClientId(facesContext);
    }

    /**
     * Gets the parent of the supplied uiComponent that is of the supplied type.
     * <br>Getting the parent form for example can be achieved by the following
     * code: UIForm uiForm = JsfTreeUtil.getParent(uiComponent, UIForm.class);
     * @param <T> The type the parent of the supplied uiComponent must have.
     * @param uiComponent The UIComponent for which the parent component must be
     *  located.
     * @param type The type of the parent component.
     * @return The parent of the supplied uiComponent or null when no parent
     *  component of the supplied type exists.
     * @throws NullPointerException When the supplied uiComponent is null.
     */
    public static <T extends UIComponent> T getParent(final UIComponent uiComponent, final Class<T> type) {
        CDebug.checkParameterNotNull(uiComponent, "uiComponent");
        UIComponent parent = uiComponent.getParent();
        while (parent != null) {
            if (type.isAssignableFrom(parent.getClass())) {
                @SuppressWarnings("unchecked") final T foundParent = (T) parent;
                return foundParent;
            }
            parent = parent.getParent();
        }
        return null;
    }

    /**
     * Walks the component tree.
     * @param uiViewRoot The UIViewRoot to walk.
     * @param uiComponentCallBack For each UIComponent found in the supplied
     *  uiViewRoot, the {@link UIComponentCallBack#call(UIComponent, int)}
     *  method is called.
     * @throws NullPointerException When either the supplied uiViewRoot or
     *  uiComponentCallBack is null.
     */
    public static void walkTheTree(final UIViewRoot uiViewRoot, final UIComponentCallBack uiComponentCallBack) {
        CDebug.checkParameterNotNull(uiViewRoot, "uiViewRoot");
        CDebug.checkParameterNotNull(uiComponentCallBack, "uiComponentCallBack");
        uiComponentCallBack.call(uiViewRoot, 0);
        walkTheTree(uiViewRoot.getChildren(), 0, uiComponentCallBack);
    }

    /**
     * Walks the component tree starting with the supplied uiComponents.
     * @param uiComponents Current list of UIComponent objects.
     * @param level The current level (of indentation).
     * @param uiComponentCallBack For each UIComponent found in the supplied
     *  uiComponents (or it's children), the
     *  {@link UIComponentCallBack#call(UIComponent, int)} method is called.
     */
    private static void walkTheTree(final List<UIComponent> uiComponents, final int level, final UIComponentCallBack uiComponentCallBack) {
        for (final UIComponent uiComponent : uiComponents) {
            uiComponentCallBack.call(uiComponent, level);
            if (uiComponent.getChildCount() > 0) {
                walkTheTree(uiComponent.getChildren(), level + 1, uiComponentCallBack);
            }
        }
    }

    /**
     * Finds all UI components of a certain type.
     * Example:<pre><code>
     *   final List&gt;UIPanel> panels = JsfTreeUtil.findAll(JsfUtil.getViewRoot(), UIPanel.class);
     * </code></pre>
     * The code above locates all UIComponent objects of type UIPanel.
     * @param <T> The type of the UIComponent to find.
     * @param uiViewRoot The UIViewRoot to search.
     * @param type The type of UIComponent to search.
     * @return A List containing all the found UIComponent objects of the
     *  supplied type.
     * @throws NullPointerException When either the supplied uiViewRoot or type
     *  is null.
     */
    public static <T extends UIComponent> List<T> findAll(final UIViewRoot uiViewRoot, final Class<T> type) {
        CDebug.checkParameterNotNull(uiViewRoot, "uiViewRoot");
        CDebug.checkParameterNotNull(type, "type");
        final List<T> result = new ArrayList<T>();
        walkTheTree(uiViewRoot, new UIComponentCallBack() {

            /**
             * Filters all components.
             * @param uiComponent The component in the tree.
             * @param level The nesting level of the component.
             */
            @SuppressWarnings("unchecked")
            @Override
            public void call(final UIComponent uiComponent, final int level) {
                if (type.isAssignableFrom(uiComponent.getClass())) {
                    result.add((T) uiComponent);
                }
            }
        });
        return result;
    }

    /**
     * Finds all UI components of a certain type that have an attribute with the
     * supplied attributeName.
     * <br>Note: This method won't find the properties of a component. For
     * example in: &lt;h:outputLabel for="idFor" value="..."/&gt;, the for
     * property wont't be found by this method.
     * Example:<pre><code>
     *   final List&gt;UIPanel> panels = JsfTreeUtil.findAll(
     *       JsfUtil.getViewRoot(), UIInput.class, "attributeName");
     * </code></pre>
     * The code above locates all UIComponent objects of type UIInput that have
     * an attribute named "attributeName".
     * @param <T> The type of the UIComponent to find.
     * @param uiViewRoot The UIViewRoot to search.
     * @param type The type of UIComponent to search.
     * @param attributeName The name of the attribute.
     * @return A List containing all the found UIComponent objects of the
     *  supplied type with an attribute named attributeName.
     * @throws IllegalArgumentException When the supplied attributeName is
     *  empty.
     * @throws NullPointerException When either the supplied uiViewRoot or type
     *  or attributeName is null.
     */
    public static <T extends UIComponent> List<T> findAll(final UIViewRoot uiViewRoot, final Class<T> type, final String attributeName) {
        CDebug.checkParameterNotNull(uiViewRoot, "uiViewRoot");
        CDebug.checkParameterNotNull(type, "type");
        CDebug.checkParameterNotEmpty(attributeName, "attributeName");
        final List<T> result = new ArrayList<T>();
        walkTheTree(uiViewRoot, new UIComponentCallBack() {

            /**
             * Filters all components with the attribute supplied above.
             * @param uiComponent The component in the tree.
             * @param level The nesting level of the component.
             */
            @SuppressWarnings("unchecked")
            @Override
            public void call(final UIComponent uiComponent, final int level) {
                if (type.isAssignableFrom(uiComponent.getClass()) && uiComponent.getAttributes().containsKey(attributeName)) {
                    result.add((T) uiComponent);
                }
            }
        });
        return result;
    }

    /**
     * Utility method to convert the "component id" as used in a for attribute
     * to a client id.
     * <br />For a developer it's easier to supply a component id than a client
     * id in a JSF page. This method aids in converting a component id to a
     * client id. For example:<pre>
     * &lt;h:outputLabel for="idAge" value="Age:" /&gt;
     * &lt;h:panelGroup&gt;
     *   &lt;h:inputText id="idAge"&gt;
     *     &lt;f:validateLength minimum="1" maximum="3" /&gt;
     *     &lt;f:validateLongRange minimum="0" maximum="120" /&gt;
     *   &lt;/h:inputText&gt;
     *   &lt;jf:message for="idAge" /&gt;
     * &lt;/h:panelGroup&gt;</pre>
     * In the previous example the componentForId would contain "idAge" and the
     * component would be the message component.
     * @param context The FacesContext.
     * @param componentForId The value of a for attribute containing a component
     *  id.<br />
     *  It is not an error when this parameter already contains a complete
     *  client id.
     * @param component The component that contains the for attribute.
     * @return The client id of the component that corresponds to the supplied
     *  componentForId.<br />
     *  When the supplied componentForId already contains a client id, the
     *  returned value is equal to the supplied componentForId.
     * @throws NullPointerException When either the supplied context,
     *  componentForId or component is null.
     * @throws IllegalArgumentException When the supplied componentForId is
     *  empty.
     */
    public static String convertComponentForToClientFor(final FacesContext context, final String componentForId, final UIComponent component) {
        CDebug.checkParameterNotNull(context, "context");
        CDebug.checkParameterNotEmpty(componentForId, "componentForId");
        CDebug.checkParameterNotNull(component, "component");
        StringBuilder clientForId = new StringBuilder(componentForId);
        if (componentForId.indexOf(UINamingContainer.getSeparatorChar(context)) == -1) {
            final String clientId = component.getClientId(context);
            final int separator = clientId.indexOf(UINamingContainer.getSeparatorChar(context));
            if (separator != -1) {
                clientForId.insert(0, UINamingContainer.getSeparatorChar(context));
                clientForId.insert(0, clientId);
            }
        }
        return clientForId.toString();
    }

    /**
     * Finds the component with the supplied id.
     * @param startingComponent The component to start the search from.
     * @param id The id of the component to search for.
     * @return The component with the supplied id or null when no such
     *  component exists.
     * @throws NullPointerException When the supplied id is null.
     * @throws IllegalArgumentException When the supplied id is empty.
     */
    public static UIComponent findComponent(final UIComponent startingComponent, final String id) {
        CDebug.checkParameterNotEmpty(id, "id");
        UIComponent parent = startingComponent;
        while (parent != null) {
            final UIComponent result = parent.findComponent(id);
            if (result != null) {
                return result;
            }
            parent = parent.getParent();
        }
        return null;
    }

    /**
     * Creates a String representation of the supplied uiViewRoot.
     * @param uiViewRoot The UIViewRoot to create a String representation of.
     * @return The component tree as a String.
     *  <br>The format of the returned String can be changed without notice.
     * @throws NullPointerException When the supplied uiViewRoot is null.
     */
    public static String toString(final UIViewRoot uiViewRoot) {
        CDebug.checkParameterNotNull(uiViewRoot, "uiViewRoot");
        final StringBuilder tree = new StringBuilder();
        walkTheTree(uiViewRoot, new UIComponentCallBack() {

            /**
             * Adds all components to the tree.
             * @param uiComponent The component in the tree.
             * @param level The nesting level of the component.
             */
            public void call(final UIComponent uiComponent, final int level) {
                tree.append(CString.repeat(">", level)).append(JsfTreeUtil.toString(uiComponent)).append("\n");
            }
        });
        return tree.toString();
    }

    /**
     * Creates a String representation of the supplied uiComponent.
     * @param uiComponent The UI Component to create a String representation of.
     * @return A String representation of the supplied  uiComponent.
     */
    public static String toString(final UIComponent uiComponent) {
        final StringBuilder result = new StringBuilder(uiComponent.getClass().getSimpleName());
        result.append(" id=" + uiComponent.getId());
        result.append(" Attributes:");
        final Map<String, Object> attributes = uiComponent.getAttributes();
        for (final Map.Entry<String, Object> entry : attributes.entrySet()) {
            result.append("[" + entry.getKey() + "=" + entry.getValue().toString() + "]");
        }
        result.append(" Facets:");
        final Map<String, UIComponent> facets = uiComponent.getFacets();
        for (final Map.Entry<String, UIComponent> entry : facets.entrySet()) {
            result.append("{" + JsfTreeUtil.toString(entry.getValue()) + "}");
        }
        return result.toString();
    }
}
