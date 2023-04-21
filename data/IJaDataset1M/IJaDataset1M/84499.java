package uk.ac.reload.straker.datamodel.learningdesign;

import java.util.Hashtable;

/**
 * Get some friendly names for xml elements
 * 
 * @author Phillip Beauvoir
 * @version $Id: LD_ElementNames.java,v 1.6 2006/07/10 11:50:48 phillipus Exp $
 */
public class LD_ElementNames {

    /**
     * Lookup table
     */
    private static Hashtable table = new Hashtable();

    static String[] names = { "act", "Act", "act-ref", "Act Reference", "activities", "Activities", "activity-description", "Activity Description", "activity-structure", "Activity Structure", "activity-structure-ref", "Activity Structure Reference", "and", "And", "calculate", "Calculate", "change-property-value", "Change Property Value", "class", "Class", "conditions", "Conditions", "complete-act", "Complete Act", "complete-activity", "Complete Activity", "complete-play", "Complete Play", "complete-unit-of-learning", "Complete Unit of Learning", "conference", "Conference Service", "current-datetime", "Current DateTime", "complete", "Complete", "datetime-activity-started", "DateTime Activity Started", "divide", "Divide", "else", "Else", "email-data", "E-Mail Data", "email-property-ref", "E-mail Property Reference", "environment", "Environment", "environment-ref", "Environment Reference", "environments", "Environments", "expression", "Expression", "feedback-description", "Feedback Description", "glob-property", "Global Property", "globpers-property", "Global Personal Property", "greater-than", "Greater Than", "hide", "Hide", "if", "If", "index-search", "Index Search Service", "information", "Information", "is", "Is", "is-not", "Is Not", "is-member-of-role", "Is Member of Role", "item-ref", "Item Reference", "langstring", "LangString", "learner", "Learner Role", "learning-activity", "Learning Activity", "learning-activity-ref", "Learning Activity Reference", "learning-object", "Learning Object", "learning-objectives", "Learning Objectives", "less-than", "Less Than", "loc-property", "Local Property", "locpers-property", "Local Personal Property", "locrole-property", "Local Role Property", "method", "Method", "monitor", "Monitor Service", "multiply", "Multiply", "not", "Not", "notification", "Notification", "no-value", "No Value", "on-completion", "On Completion", "or", "Or", "prerequisites", "Prerequisites", "play", "Play", "play-ref", "Play Reference", "properties", "Properties", "property-ref", "Property Reference", "property-group", "Property Group", "property-group-ref", "Property Group Reference", "property-value", "Property Value", "role-part", "Role Part", "role-part-ref", "Role Part Reference", "role-ref", "Role Reference", "send-mail", "Send Mail Service", "show", "Show", "staff", "Staff Role", "subject", "Subject", "subtract", "Subtract", "sum", "Sum", "support-activity", "Support Activity", "support-activity-ref", "Support Activity Reference", "then", "Then", "time-unit-of-learning-started", "Time Unit Of Learning Started", "title", "Title", "unit-of-learning-href", "Unit of Learning HREF", "username-property-ref", "User Name Property Reference", "users-in-role", "Users in Role", "when-condition-true", "When Condition True", "when-property-value-is-set", "When Property Value Is Set", "with-control", "With Control" };

    static {
        for (int i = 0; i < names.length; i += 2) {
            table.put(names[i], names[i + 1]);
        }
    }

    /**
     * @param elementName
     * @return A friendly name
     */
    public static String getElementFriendlyName(String elementName) {
        if (elementName == null) {
            return "";
        }
        String name = (String) table.get(elementName);
        return name == null ? elementName : name;
    }
}
