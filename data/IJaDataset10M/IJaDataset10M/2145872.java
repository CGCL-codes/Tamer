package redora.configuration.rdo.businessrules;

import redora.configuration.rdo.businessrules.base.RedoraConfigurationBusinessRulesBase;
import redora.service.BusinessRuleViolation;

/**
* Custom business rule implementations go here. For a business rule you must first
* define them in the object model (see <a href="http://code.google.com/p/redora/wiki/BusinessRules">the manual</a>).
* The generator will then generate a ruleId handle on RedoraConfigurationBusinessRulesBase like:
* <code>
* RedoraConfigurationBusinessRules.BR_[number]
* </code>
* which can be used to add a new BusinessRuleViolation. For example like this:
* <code>
  new BusinessRuleViolation(RedoraConfiguration
        , RedoraConfigurationFields.myNaughtyField
        , RedoraConfigurationBusinessRules.BR_1
        , BusinessRuleViolation.Action.Update));
* </code>
* As you can image the BusinessRuleViolation object make it possible to pinpoint the
* violate rule to the end user. In the object model you can set messages (with parameters)
* in any of the languages you want to support. The ruleId handle, RedoraConfigurationBusinessRules.BR_1
* in this example will point to the message you have define in the model, that could be something like:
<code>
    &lt;businessRules&gt;
        &lt;businessRule number="1" javadoc="Only updates at night are allowed." attributes="myNaughtyField"&gt;
            &lt;message language="en"&gt;You are too early, come back at {0}&lt;/message&gt;
        &lt;/businessRule&gt;
    &lt;/businessRules&gt;
</code>
* You can use RedoraConfigurationBusinessRules by invoking the static check() method.
* @see BusinessRuleViolation
* @author Redora (www.redora.net)
*/
public class RedoraConfigurationBusinessRules extends RedoraConfigurationBusinessRulesBase {
}
