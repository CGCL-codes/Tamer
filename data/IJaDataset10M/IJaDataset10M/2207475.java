package org.apache.wicket.examples.ajax.builtin.modal;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.examples.ajax.builtin.BasePage;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.PropertyModel;

/**
 * @author Matej Knopp
 */
public class ModalWindowPage extends BasePage {

    /**
	 */
    public ModalWindowPage() {
        final Label result;
        add(result = new Label("result", new PropertyModel(this, "result")));
        result.setOutputMarkupId(true);
        final ModalWindow modal1;
        add(modal1 = new ModalWindow("modal1"));
        modal1.setPageMapName("modal-1");
        modal1.setCookieName("modal-1");
        modal1.setPageCreator(new ModalWindow.PageCreator() {

            public Page createPage() {
                return new ModalContent1Page(ModalWindowPage.this, modal1);
            }
        });
        modal1.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {

            public void onClose(AjaxRequestTarget target) {
                target.addComponent(result);
            }
        });
        modal1.setCloseButtonCallback(new ModalWindow.CloseButtonCallback() {

            public boolean onCloseButtonClicked(AjaxRequestTarget target) {
                setResult("Modal window 1 - close button");
                return true;
            }
        });
        add(new AjaxLink("showModal1") {

            public void onClick(AjaxRequestTarget target) {
                modal1.show(target);
            }
        });
        final ModalWindow modal2;
        add(modal2 = new ModalWindow("modal2"));
        modal2.setContent(new ModalPanel1(modal2.getContentId()));
        modal2.setTitle("This is modal window with panel content.");
        modal2.setCookieName("modal-2");
        modal2.setCloseButtonCallback(new ModalWindow.CloseButtonCallback() {

            public boolean onCloseButtonClicked(AjaxRequestTarget target) {
                setResult("Modal window 2 - close button");
                return true;
            }
        });
        modal2.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {

            public void onClose(AjaxRequestTarget target) {
                target.addComponent(result);
            }
        });
        add(new AjaxLink("showModal2") {

            public void onClick(AjaxRequestTarget target) {
                modal2.show(target);
            }
        });
    }

    /**
	 * @return the result
	 */
    public String getResult() {
        return result;
    }

    /**
	 * @param result
	 *            the result to set
	 */
    public void setResult(String result) {
        this.result = result;
    }

    private String result;
}
