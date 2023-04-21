package de.swm.commons.mobile.client.widgets;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;
import de.swm.commons.mobile.client.SWMMobile;
import de.swm.commons.mobile.client.utils.Utils;

/**
 * An AccordionPanel is an widget, that can be expanded or collapsed. The stack is the content of such a panel.
 * 
 * <pre>
 * <m:AccordionPanel ui:field="accordion">
 * 		<m:AccordionStack>
 * 			<m:AccordionHeader>
 * 				<g:Label>Accordion Stack 1</g:Label>
 * 			</m:AccordionHeader>
 * 			<m:AccordionContent>
 * 				<g:Label>Accordion Stack 1 Content</g:Label>
 * 				<g:Label>Accordion Stack 4 Content</g:Label>
 * 			</m:AccordionContent>
 * 		</m:AccordionStack>
 * 	</m:AccordionPanel>
 * </pre>
 * 
 */
public class AccordionStack extends PanelBase {

    private AccordionHeader myHeader;

    private AccordionContent myContent;

    @Override
    public void onInitialLoad() {
        if (this.getStyleName().indexOf(SWMMobile.getTheme().getMGWTCssBundle().getAccordionPanelCss().expand()) == -1) {
            close();
        }
    }

    @Override
    public void add(Widget w) {
        if (myHeader == null) {
            myHeader = (AccordionHeader) w;
        } else if (myContent != null) {
            assert false : "Can only contain a header and a content.";
        } else {
            myContent = (AccordionContent) w;
        }
        myFlowPanel.add(w);
    }

    /**
	 * Will close this stack.
	 */
    public void close() {
        this.addStyleName(SWMMobile.getTheme().getMGWTCssBundle().getAccordionPanelCss().collapse());
        this.removeStyleName(SWMMobile.getTheme().getMGWTCssBundle().getAccordionPanelCss().expand());
        Element focus = Utils.getActiveElement();
        focus.blur();
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {

            @Override
            public void execute() {
                myContent.setHeight("0px");
            }
        });
    }

    /**
	 * Will expand this stack.
	 */
    public void expand() {
        if (isExpended()) {
            return;
        }
        this.addStyleName(SWMMobile.getTheme().getMGWTCssBundle().getAccordionPanelCss().expand());
        this.removeStyleName(SWMMobile.getTheme().getMGWTCssBundle().getAccordionPanelCss().collapse());
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {

            @Override
            public void execute() {
                Utils.console(myContent.getElement().getScrollHeight() - myContent.getElement().getOffsetHeight() + "px");
                myContent.setHeight(myContent.getElement().getScrollHeight() - myContent.getElement().getOffsetHeight() + "px");
            }
        });
    }

    /**
	 * Will invert the stack (close if opened, close if open).
	 */
    public void toggle() {
        if (!isExpended()) {
            expand();
        } else {
            close();
        }
    }

    public boolean isExpended() {
        return this.getStyleName().indexOf(SWMMobile.getTheme().getMGWTCssBundle().getAccordionPanelCss().expand()) > -1;
    }

    public AccordionHeader getHeader() {
        return myHeader;
    }

    public AccordionContent getContent() {
        return myContent;
    }
}
