package ru.curs.showcase.app.client;

import ru.beta2.extra.gwt.ui.panels.DialogBoxWithCaptionButton;
import ru.curs.showcase.app.client.api.*;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.*;
import com.google.gwt.user.client.ui.*;

/**
 * 
 * Класс окна для отображения элементов информационной панели различных типов.
 * 
 * @author anlug
 * 
 */
public class WindowWithDataPanelElement extends DialogBoxWithCaptionButton {

    /**
	 * ScrollPanel, которая содержит содержимое модального окна.
	 */
    private final ScrollPanel sp = new ScrollPanel();

    /**
	 * BasicElementPanel bep.
	 */
    private BasicElementPanel bep = null;

    /**
	 * Переменная определяющая показывать ли внизу модального окна кнопку
	 * Закрыть.
	 */
    private Boolean showCloseBottomButton;

    /**
	 * @param ashowCloseBottomButton
	 *            the showCloseBottomButton to set
	 */
    public final void setShowCloseBottomButton(final Boolean ashowCloseBottomButton) {
        this.showCloseBottomButton = ashowCloseBottomButton;
    }

    /**
	 * @return the showCloseBottomButton
	 */
    public Boolean getShowCloseBottomButton() {
        return showCloseBottomButton;
    }

    public WindowWithDataPanelElement(final Boolean ashowCloseBottomButton) {
        super();
        setShowCloseBottomButton(ashowCloseBottomButton);
        final int n100 = 100;
        sp.setSize(String.valueOf(Window.getClientWidth() - n100) + "px", String.valueOf(Window.getClientHeight() - n100) + "px");
    }

    public WindowWithDataPanelElement(final String caption, final Boolean ashowCloseBottomButton) {
        super(caption);
        setShowCloseBottomButton(ashowCloseBottomButton);
        final int n100 = 100;
        sp.setSize(String.valueOf(Window.getClientWidth() - n100) + "px", String.valueOf(Window.getClientHeight() - n100) + "px");
    }

    public WindowWithDataPanelElement(final String caption, final Integer width, final Integer heigth, final Boolean ashowCloseBottomButton) {
        super(caption);
        sp.setSize(String.valueOf(width) + "px", String.valueOf(heigth) + "px");
        setShowCloseBottomButton(ashowCloseBottomButton);
    }

    /**
	 * Показывает модальное окно с переданной ему BasicElementPanel и заголовком
	 * окна.
	 * 
	 * @param bep1
	 *            - BasicElementPanel, который будет отображаться в окне.
	 */
    public void showModalWindow(final BasicElementPanel bep1) {
        bep = bep1;
        if (bep instanceof XFormPanel) {
            XFormPanel.beforeModalWindow(bep);
        }
        VerticalPanel dialogContents = new VerticalPanel();
        DOM.setElementAttribute(dialogContents.getElement(), "id", "showcaseModalWindow");
        sp.add(dialogContents);
        final int n = 10;
        dialogContents.setSpacing(n);
        dialogContents.setSize("100%", "100%");
        setWidget(sp);
        setAnimationEnabled(true);
        setGlassEnabled(true);
        dialogContents.add(bep.getPanel());
        if (getShowCloseBottomButton()) {
            Button ok = new Button("Закрыть");
            ok.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(final ClickEvent event) {
                    closeWindow();
                }
            });
            dialogContents.add(ok);
            dialogContents.setCellHorizontalAlignment(ok, HasHorizontalAlignment.ALIGN_RIGHT);
            ok.setFocus(true);
        }
        AppCurrContext.getInstance().setCurrentOpenWindowWithDataPanelElement(this);
        center();
        show();
    }

    /**
	 * Скрывает модальное окно.
	 */
    @Override
    public void closeWindow() {
        super.closeWindow();
        AppCurrContext.getInstance().setCurrentOpenWindowWithDataPanelElement(null);
        if (bep != null) {
            if (bep instanceof XFormPanel) {
                XFormPanel.destroyXForms();
            }
        }
        bep = null;
        ActionExecuter.drawXFormPanelsAfterModalWindowShown(AppCurrContext.getInstance().getCurrentAction());
    }
}
