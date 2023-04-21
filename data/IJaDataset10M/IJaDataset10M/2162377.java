package org.elucida.common;

import java.io.Serializable;
import java.util.*;

/**
 * @author Elucida Software
 * @version 1.0
 * 
 */
public interface Result extends Serializable {

    public boolean hasErrors();

    public boolean success();

    public boolean hasWarnings();

    public List getAllMessages();

    public List getInfoMessages();

    public List getErrorMessages();

    public List getWarningMessages();

    public void addMessage(Message message);

    public void addMessages(List messages);

    public void addInfoMessage(String messageText);

    public void addErrorMessage(String messageText);

    public void reset();

    public Object getResultObject();

    public void setResultObject(Object obj);

    public String getOutputView();

    public void setOutputView(String outputView);

    public class ResultImpl implements Result {

        public List messages = new ArrayList();

        public Object resultData;

        protected String outputView;

        public static final long serialVersionUID = 1L;

        public void addMessage(Message message) {
            messages.add(message);
        }

        public void addMessages(List messages) {
            messages.addAll(messages);
        }

        public void addInfoMessage(String messageText) {
            if (messageText != null && messageText.length() > 0) {
                Message msg = Message.Factory.createInfo(messageText);
                messages.add(msg);
            }
        }

        public void addErrorMessage(String messageText) {
            if (messageText != null && messageText.length() > 0) {
                Message msg = Message.Factory.createError(messageText);
                messages.add(msg);
            }
        }

        public List getAllMessages() {
            return messages;
        }

        public List getErrorMessages() {
            List filteredList = new ArrayList();
            for (Iterator iter = messages.iterator(); iter.hasNext(); ) {
                Message element = (Message) iter.next();
                if (element.isError()) {
                    filteredList.add(element);
                }
            }
            return filteredList;
        }

        public List getInfoMessages() {
            List filteredList = new ArrayList();
            for (Iterator iter = messages.iterator(); iter.hasNext(); ) {
                Message element = (Message) iter.next();
                if (element.isInfo()) {
                    filteredList.add(element);
                }
            }
            return filteredList;
        }

        public Object getResultObject() {
            return resultData;
        }

        public List getWarningMessages() {
            List filteredList = new ArrayList();
            for (Iterator iter = messages.iterator(); iter.hasNext(); ) {
                Message element = (Message) iter.next();
                if (element.isWarning()) {
                    filteredList.add(element);
                }
            }
            return filteredList;
        }

        public boolean hasErrors() {
            if (getErrorMessages().size() > 0) {
                return true;
            } else {
                return false;
            }
        }

        public boolean hasWarnings() {
            if (getWarningMessages().size() > 0) {
                return true;
            } else {
                return false;
            }
        }

        public void reset() {
            messages.clear();
            resultData = null;
        }

        public void setResultObject(Object obj) {
            resultData = obj;
        }

        public boolean success() {
            return !hasErrors();
        }

        /**
		 * @return Returns the outputView.
		 */
        public String getOutputView() {
            return outputView;
        }

        /**
		 * @param outputView The outputView to set.
		 */
        public void setOutputView(String outputView) {
            this.outputView = outputView;
        }
    }

    public static class Factory {

        public static Result create() {
            return new ResultImpl();
        }
    }
}
