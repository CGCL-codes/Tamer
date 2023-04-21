package org.fulworx.core.rest;

import org.fulworx.core.config.Descriptor;
import org.fulworx.core.config.restlet.RestfulMethodDescriptor;
import org.fulworx.core.config.restlet.bean.RestfulMethodDescriptorBean;
import org.fulworx.core.config.restlet.bean.AccessorBean;
import org.fulworx.core.config.restlet.bean.DescriptorBean;
import org.fulworx.core.descriptor.action.CreateAction;
import org.fulworx.core.descriptor.action.DeleteAction;
import org.fulworx.core.descriptor.action.EntityAction;
import org.fulworx.core.descriptor.action.ReadAction;
import org.fulworx.core.descriptor.action.UpdateAction;
import org.fulworx.core.invoker.ActionInvoker;
import org.fulworx.core.invoker.xwork.DefaultActionResult;
import org.fulworx.core.rest.restlet.ActionInvokerRestlet;
import org.fulworx.core.rest.restlet.handler.ActionInvokerHandler;
import org.fulworx.core.rest.restlet.representation.CoreRepresentationBuilder;
import org.fulworx.core.rest.restlet.representation.StringRepresentationBuilder;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.restlet.Context;
import org.restlet.data.Method;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.StringRepresentation;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @version $Id: $
 */
public class TestActionInvokerRestlet extends MockObjectTestCase {

    public void testInvocation() throws IOException {
        Mock mockInvoker = mock(ActionInvoker.class);
        List<RestfulMethodDescriptor> methods = new ArrayList();
        methods.add(new RestfulMethodDescriptorBean("create", (ActionInvoker) mockInvoker.proxy(), MyAction.class));
        methods.add(new RestfulMethodDescriptorBean("read", (ActionInvoker) mockInvoker.proxy(), MyAction.class));
        methods.add(new RestfulMethodDescriptorBean("update", (ActionInvoker) mockInvoker.proxy(), MyAction.class));
        methods.add(new RestfulMethodDescriptorBean("delete", (ActionInvoker) mockInvoker.proxy(), MyAction.class));
        mockInvoker.expects(once()).method("invoke").withAnyArguments().will(returnValue(new DefaultActionResult("ThisIsMyResultEntity")));
        CoreRepresentationBuilder coreRepresentationCreator = new CoreRepresentationBuilder();
        coreRepresentationCreator.put("panelist/{panelistid}", new StringRepresentationBuilder() {
        });
        Descriptor descriptor = new DescriptorBean(coreRepresentationCreator, "", MyAction.class, new AccessorBean(), "panelist/{panelistid}", methods);
        ActionInvokerRestlet actionInvokerRestlet = new MyActionInvokerRestlet(coreRepresentationCreator, new Context(), descriptor);
        Request request = new Request();
        request.setEntity(new StringRepresentation("ThisIsMyEntity"));
        request.setMethod(Method.GET);
        request.setResourceRef("panelist/bob");
        Response response = new Response(request);
        actionInvokerRestlet.handle(request, response);
        assertEquals("ThisIsMyResultEntity", response.getEntity().getText());
    }

    private class MyActionInvokerRestlet extends ActionInvokerRestlet {

        public MyActionInvokerRestlet(CoreRepresentationBuilder coreRepresentationCreator, Context context, Descriptor descriptor) {
            super(context, new ActionInvokerHandler("", descriptor, context), coreRepresentationCreator);
        }
    }

    private class MyAction implements EntityAction, CreateAction, ReadAction, UpdateAction, DeleteAction {

        public String create() {
            return null;
        }

        public String execute() throws Exception {
            return null;
        }

        public String read() {
            return null;
        }

        public String update() {
            return null;
        }

        public String delete() {
            return null;
        }

        public Object getEntity() {
            return null;
        }
    }
}
