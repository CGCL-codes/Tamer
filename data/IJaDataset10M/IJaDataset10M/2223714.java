package com.antilia.web.button;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * 
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 */
public abstract class JavaScriptButton extends Panel implements IMenuItem {

    private static final long serialVersionUID = 1L;

    public static final int NO_ORDER = -1;

    private Button link;

    private boolean ajaxButton = false;

    private int order = NO_ORDER;

    /**
	 * Constructor.
	 * @param id
	 */
    public JavaScriptButton(String id) {
        this(id, false, NO_ORDER);
    }

    /**
	 * Constructor.
	 * @param id
	 */
    public JavaScriptButton(String id, int order) {
        this(id, false, order);
    }

    /**
	 * Constructor.
	 * @param id
	 */
    public JavaScriptButton(String id, boolean ajaxButton) {
        this(id, ajaxButton, NO_ORDER);
    }

    /**
	 * Constructor.
	 * @param id
	 */
    public JavaScriptButton(String id, boolean ajaxButton, int order) {
        super(id);
        this.ajaxButton = ajaxButton;
        link = newLink("link");
        add(link);
        Image image = newImage("image");
        link.add(image);
        link.add(newLabel("label"));
    }

    /**
	 * Override this method if you want to rpovide your own implementation of 
	 * the a link (the inner button of the panel).
	 * 
	 * @param id
	 * @return
	 */
    protected Button newLink(String id) {
        if (isAjaxButton()) {
            return new IndicatingAjaxSubmitButton(id) {

                private static final long serialVersionUID = 1L;

                @Override
                protected void onSubmit(AjaxRequestTarget target, Form form) {
                    JavaScriptButton.this.onSubmit(target, form);
                }

                @Override
                protected IAjaxCallDecorator getAjaxCallDecorator() {
                    return JavaScriptButton.this.getAjaxCallDecorator();
                }
            };
        }
        return new Button(id) {

            private static final long serialVersionUID = 1L;

            @Override
            public void onSubmit() {
                JavaScriptButton.this.onSubmit();
            }
        };
    }

    /**
	 * Callback method for Ajax buttons.
	 * 
	 * @param target
	 * @param form
	 */
    protected void onSubmit(AjaxRequestTarget target, Form form) {
    }

    /**
	 * Callback method for normal buttons.
	 *
	 */
    public void onSubmit() {
    }

    /**
	 * Overide this method to add your own AjaxCallDecorator.
	 * @return
	 */
    protected IAjaxCallDecorator getAjaxCallDecorator() {
        return null;
    }

    /**
	 * Override this method  to provide your own image.
	 * 
	 * @param id
	 * @return
	 */
    protected Image newImage(String id) {
        return new Image(id, getImage());
    }

    protected Label newLabel(String id) {
        return new Label(id, getLabel());
    }

    protected abstract String getLabel();

    protected abstract ResourceReference getImage();

    /**
	 * @return the ajaxButton
	 */
    public boolean isAjaxButton() {
        return ajaxButton;
    }

    /**
	 * @param ajaxButton the ajaxButton to set
	 */
    public void setAjaxButton(boolean ajaxButton) {
        this.ajaxButton = ajaxButton;
    }

    /**
	 * @return the order
	 */
    public int getOrder() {
        return order;
    }

    /**
	 * @param order the order to set
	 */
    public void setOrder(int order) {
        this.order = order;
    }
}
