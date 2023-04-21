package com.volantis.mcs.papi.impl;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.TestEnvironmentContext;
import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.context.TestMarinerRequestContext;
import com.volantis.mcs.expression.ExpressionSupport;
import com.volantis.mcs.expression.MCSExpressionHelper;
import com.volantis.mcs.layouts.CanvasLayout;
import com.volantis.mcs.layouts.Form;
import com.volantis.mcs.layouts.TestPane;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.papi.PAPIConstants;
import com.volantis.mcs.papi.PAPIElement;
import com.volantis.mcs.papi.XFTextInputAttributes;
import com.volantis.mcs.protocols.CanvasAttributes;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.VolantisProtocolStub;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.protocols.forms.FormDescriptor;
import com.volantis.mcs.protocols.forms.AbstractForm;
import com.volantis.mcs.protocols.layouts.PaneInstance;
import com.volantis.mcs.protocols.layouts.TestPaneInstance;
import com.volantis.mcs.protocols.layouts.FormInstance;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayout;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayoutTestHelper;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolverTestHelper;
import com.volantis.mcs.policies.variants.text.TextEncoding;
import com.volantis.mcs.devices.InternalDeviceTestHelper;
import com.volantis.styling.StylesBuilder;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.namespace.NamespaceFactory;
import junitx.util.PrivateAccessor;

/**
 * Test case for XFFormFieldElement.
 */
public abstract class XFFormFieldElementImplTestAbstract extends AbstractElementImplTestAbstract {

    /**
     * This method is difficult to test because it takes the input (parameters
     * and creates a new object (
     */
    public void testDoField() throws Exception {
        /**
         * This class is used by testDoField (the only method here so far) an is no
         * longer anonymous in order to check to see if the writeInitialFocus
         * method has been called.
         */
        class MyVolantisProtocolStub extends VolantisProtocolStub {

            boolean writeInitialFocusCalled = false;

            private CanvasAttributes myCanvasAttributes = null;

            public CanvasAttributes getCanvasAttributes() {
                if (myCanvasAttributes == null) {
                    myCanvasAttributes = new CanvasAttributes();
                    myCanvasAttributes.setStyles(StylesBuilder.getDeprecatedStyles());
                    myCanvasAttributes.setInitialFocus("ID");
                }
                return myCanvasAttributes;
            }

            public void writeInitialFocus(String tabindex) {
                writeInitialFocusCalled = true;
            }
        }
        ;
        PAPIElement element = createTestablePAPIElement();
        TestMarinerRequestContext requestContext = new TestMarinerRequestContext();
        TestMarinerPageContext pageContext = new TestMarinerPageContext();
        XFTextInputAttributes attributes = new XFTextInputAttributes();
        com.volantis.mcs.protocols.XFTextInputAttributes pattributes = new com.volantis.mcs.protocols.XFTextInputAttributes();
        attributes.setId("ID");
        attributes.setStyleClass("StyleClass");
        attributes.setTitle("Title");
        attributes.setCaption("Caption");
        attributes.setHelp("Help");
        attributes.setName("Name");
        attributes.setPrompt("Prompt");
        attributes.setShortcut("Shortcut");
        attributes.setTabindex("1");
        XFFormFieldElementImpl xfElement = (XFFormFieldElementImpl) element;
        pageContext.setPolicyReferenceResolver(PolicyReferenceResolverTestHelper.getCommonExpectations(expectations, mockFactory));
        MyVolantisProtocolStub protocol = new MyVolantisProtocolStub();
        pageContext.setProtocol(protocol);
        ContextInternals.setMarinerPageContext(requestContext, pageContext);
        pageContext.pushRequestContext(requestContext);
        protocol.setMarinerPageContext(pageContext);
        assertNotNull(pattributes);
        assertNull(pattributes.getCaptionContainerInstance());
        assertNull(pattributes.getEntryContainerInstance());
        assertNull(pattributes.getId());
        assertNull(pattributes.getTitle());
        assertEquals(protocol.writeInitialFocusCalled, false);
        xfElement.doField(pageContext, attributes, pattributes);
        assertEquals(protocol.writeInitialFocusCalled, true);
        assertNotNull(pattributes);
        assertNull(pattributes.getCaptionContainerInstance());
        assertNull(pattributes.getEntryContainerInstance());
        assertEquals(pattributes.getId(), attributes.getId());
        assertEquals(pattributes.getTitle(), attributes.getTitle());
        assertEquals(pattributes.getCaption().getText(TextEncoding.PLAIN), attributes.getCaption());
        assertEquals(pattributes.getHelp().getText(TextEncoding.PLAIN), attributes.getHelp());
        assertEquals(pattributes.getName(), attributes.getName());
        assertEquals(pattributes.getPrompt().getText(TextEncoding.PLAIN), attributes.getPrompt());
        assertEquals(pattributes.getShortcut().getText(TextEncoding.PLAIN), attributes.getShortcut());
        assertNull(pattributes.getFieldDescriptor());
        assertEquals(pattributes.getTabindex(), attributes.getTabindex());
    }

    /**
     * Test the method: checkPaneInstances
     */
    public void testCheckPanes() throws Exception {
        XFFormFieldElementImpl element = (XFFormFieldElementImpl) createTestablePAPIElement();
        TestMarinerPageContext pageContext = new TestMarinerPageContext();
        TestMarinerRequestContext requestContext = new TestMarinerRequestContext();
        TestEnvironmentContext environmentContext = new TestEnvironmentContext();
        ProtocolBuilder builder = new ProtocolBuilder();
        DOMProtocol protocol = (DOMProtocol) builder.build(new TestProtocolRegistry.TestDOMProtocolFactory(), InternalDeviceTestHelper.createTestDevice());
        ExpressionContext expressionContext = ExpressionFactory.getDefaultInstance().createExpressionContext(null, NamespaceFactory.getDefaultInstance().createPrefixTracker());
        ContextInternals.setEnvironmentContext(requestContext, environmentContext);
        ContextInternals.setMarinerPageContext(requestContext, pageContext);
        MCSExpressionHelper.setExpressionContext(requestContext, expressionContext);
        pageContext.pushRequestContext(requestContext);
        pageContext.setProtocol(protocol);
        ExpressionSupport.registerFunctions(expressionContext);
        expressionContext.setProperty(MarinerRequestContext.class, requestContext, true);
        protocol.setMarinerPageContext(pageContext);
        String paneName = "caption-pane";
        CanvasLayout canvasLayout = new CanvasLayout();
        TestPane pane = new TestPane(canvasLayout);
        pane.setName(paneName);
        RuntimeDeviceLayout runtimeDeviceLayout = RuntimeDeviceLayoutTestHelper.activate(canvasLayout);
        pageContext.addPaneMapping(pane);
        pageContext.setDeviceLayout(runtimeDeviceLayout);
        TestPaneInstance fContext = new TestPaneInstance();
        pageContext.setFormatInstance(fContext);
        int result = element.checkPaneInstances(pageContext, null, null);
        assertEquals(result, PAPIConstants.SKIP_ELEMENT_BODY);
        String value = "{layout:getPaneInstance('" + paneName + "',0)}";
        result = element.checkPaneInstances(pageContext, value, null);
        assertEquals(result, PAPIConstants.PROCESS_ELEMENT_BODY);
        PaneInstance captionPaneInstance = (PaneInstance) PrivateAccessor.getField(element, "captionPaneInstance");
        PaneInstance entryPaneInstance = (PaneInstance) PrivateAccessor.getField(element, "entryPaneInstance");
        assertNotNull(entryPaneInstance);
        assertNotNull(captionPaneInstance);
        assertTrue("Panes should be exactly the same", entryPaneInstance == captionPaneInstance);
        FormDescriptor descriptor = new FormDescriptor();
        XFFormElementImpl formElement = new XFFormElementImpl();
        Form form = new Form(new CanvasLayout());
        form.setName("form-name");
        FormInstance formInstance = new FormInstance(NDimensionalIndex.ZERO_DIMENSIONS);
        formInstance.setFormat(form);
        formElement.getProtocolAttributes().setFormData(formInstance);
        PrivateAccessor.setField(formElement, "formDescriptor", descriptor);
        pageContext.setCurrentElement(formElement);
        result = element.checkPaneInstances(pageContext, value, null);
        assertEquals(result, PAPIConstants.PROCESS_ELEMENT_BODY);
    }
}
