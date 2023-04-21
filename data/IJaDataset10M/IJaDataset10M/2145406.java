package com.spring.workflow.parser;

import java.io.Serializable;
import java.util.Iterator;

/**
 * This class defines the page and its actions.
 */
public class PageDefinition extends AbstractWorkflowDefinition implements Serializable {

    /**
     * The parameter containsBackAction will be set to true if one of the possible actions of this page has an back
     * option set. This means that the action can go back to the page that we were previously on. This parameter will
     * be set after the actions have all been defined and is a shortcut to avoid looping over all the actionDefinitions
     */
    private Boolean backActionEnabled;

    /**
	 * The isTokenized defines if the definition is still valid. After submitting a form (with the save option set to
	 * true), the token will be reset. When the user then enters a page with a previous token, the user will be
	 * redirected to the first page of the pageflow.
	 */
    private Boolean tokenized = null;

    public PageDefinition() {
    }

    public boolean isBackActionEnabled() {
        if (backActionEnabled == null) {
            setBackActionEnabled();
        }
        return backActionEnabled.booleanValue();
    }

    /**
     * In stead of using the slightly cryptig getParent(), you have to use the getPageFlow function here.
     * This way the function tells a bit more what is returned
     * @return PageFlowDefinition where this page belongs to
     */
    public PageFlowDefinition getPageFlow() {
        return (PageFlowDefinition) this.getParent();
    }

    public boolean equals(Object o) {
        if (o instanceof PageDefinition) {
            if (this.getFullName().equals(((PageDefinition) o).getFullName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method will return a pageDefinition based on special navigation names. These navigation names can be used in
     * wizzard type of pages. Using these special names (__first__, __next__, __previous__, __last__) the program will
     * navigate the children of the pageflow. Based on the actionName the corresponding page must be found. However the
     * list of pages is contained in the pageflow, but the action is defined on a page. Therefor it is defined here, and
     * the actual navigation is done in the pageflowDefinition
     * @param actionName
     * @return
     */
    public PageDefinition navigate(String actionName) {
        return this.getPageFlow().navigate(this, actionName);
    }

    /**
     * This method will remove the action from the action list that should be skipped. This method is called after
     * reading of the workflow.xml, during the expanding of the menus.
     */
    public void removeSkipActions() {
        for (int i = 0; i < getChildren().size(); i++) {
            if (this.isSkipAction(((ActionDefinition) getChildren().get(i)).getName())) {
                this.getChildren().remove(i);
                i--;
            }
        }
    }

    /**
     * find the action definition for this name.
     * @param name
     * @return
     */
    public ActionDefinition findAction(String name) {
        if (name == null) {
            return null;
        }
        AbstractWorkflowDefinition returnValue = null;
        for (int i = 0; i < this.getChildren().size(); i++) {
            returnValue = (AbstractWorkflowDefinition) this.getChildren().get(i);
            if (returnValue.getName().equals(name) || returnValue.getFullName().equals(name)) {
                return (ActionDefinition) returnValue;
            }
        }
        return null;
    }

    /**
	 * This pageDefinition is back enabled, when one of its action has a back action.
	 */
    public void setBackActionEnabled() {
        if (this.backActionEnabled == null) {
            Iterator it = this.getChildren().iterator();
            while (it.hasNext()) {
                ActionDefinition actionDefinition = (ActionDefinition) it.next();
                if (actionDefinition.isBack()) {
                    this.backActionEnabled = Boolean.TRUE;
                    return;
                }
            }
            this.backActionEnabled = Boolean.FALSE;
        }
    }

    /**
	 * This page must be tokenized if one of the actions contains a tokenized action.
	 * @param save
	 * @return
	 */
    public boolean isTokenized(boolean save) {
        if (tokenized == null) {
            Iterator it = this.getChildren().iterator();
            while (it.hasNext()) {
                ActionDefinition actionDefinition = (ActionDefinition) it.next();
                if (actionDefinition.isTokenized()) {
                    this.tokenized = Boolean.TRUE;
                    break;
                }
            }
            if (this.tokenized == null) {
                this.tokenized = Boolean.valueOf(save);
            }
        }
        return this.tokenized.booleanValue();
    }

    public boolean isTokenized() {
        return isTokenized(false);
    }

    public void setTokenized(boolean tokenized) {
        if (tokenized) {
            this.tokenized = Boolean.TRUE;
        } else {
            this.tokenized = Boolean.FALSE;
        }
    }
}
