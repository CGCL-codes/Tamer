package org.uefl.ldIntegration;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import org.coppercore.parser.IMSLDIfNode;
import org.coppercore.parser.IMSLDIsNode;
import org.coppercore.parser.IMSLDIsNotNode;
import org.coppercore.parser.IMSLDNode;
import org.coppercore.parser.IMSLDPropertyValueNode;
import org.uengine.contexts.TextContext;
import org.uengine.kernel.And;
import org.uengine.kernel.Condition;
import org.uengine.kernel.Evaluate;
import org.uengine.kernel.EventHandler;
import org.uengine.kernel.Or;
import org.uengine.kernel.Otherwise;
import org.uengine.kernel.ProcessVariable;
import org.uengine.kernel.ScriptActivity;
import org.uengine.kernel.SequenceActivity;
import org.uengine.kernel.SwitchActivity;
import org.uengine.kernel.ValidationContext;
import org.uengine.util.RecursiveLoop;

public class IfNode2Handler {

    IMSLDPropertyValueNode tempValueNode = null;

    IMSLDIsNode tempIsNode = null;

    HashMap nodeValue = new HashMap();

    public EventHandler makeHandler(EventHandler eventHandler, Vector ifNode, HashMap processVariable) {
        SwitchActivity switchActivity = new SwitchActivity();
        Condition[] conditions = new Condition[10];
        SequenceActivity sequenceActivity = null;
        int tempCnt = 1;
        for (int ifCnt = 0; ifCnt < ifNode.size(); ifCnt++) {
            IMSLDIfNode tempIfNode = (IMSLDIfNode) ifNode.get(ifCnt);
            if (tempIfNode.children.get(0) instanceof IMSLDIsNotNode) {
                continue;
            }
            tempIsNode = (IMSLDIsNode) tempIfNode.children.get(0);
            tempValueNode = (IMSLDPropertyValueNode) tempIsNode.children.get(0);
            sequenceActivity = new SequenceActivity();
            String value = tempValueNode.node.getFirstChild().getNodeValue();
            nodeValue.put("Answer" + tempCnt, value);
            ScriptActivity scriptActivity1 = new ScriptActivity();
            String script = "try{\n";
            script += "instance.set(\"show_class_Answer" + tempCnt + "_Wrong\", new Boolean(false));\n";
            script += "return (new Boolean(true));\n";
            script += "}catch(Exception e){\n";
            script += "throw e;\n}";
            scriptActivity1.setScript(script);
            scriptActivity1.setLanguage(ScriptActivity.LANGUAGE_JAVA);
            ScriptActivity scriptActivity2 = new ScriptActivity();
            String script2 = "try{\n";
            script2 += "instance.set(\"show_class_Answer" + tempCnt + "_Right\", new Boolean(true));\n";
            script2 += "return (new Boolean(true));\n";
            script2 += "}catch(Exception e){\n";
            script2 += "throw e;\n}";
            scriptActivity2.setScript(script2);
            scriptActivity2.setLanguage(ScriptActivity.LANGUAGE_JAVA);
            ScriptActivity scriptActivity3 = new ScriptActivity();
            String script3 = "try{\n";
            script3 += "instance.set(\"QuestionTrue" + tempCnt + "_PV\", new Boolean(true));\n";
            script3 += "return (new Boolean(true));\n";
            script3 += "}catch(Exception e){\n";
            script3 += "throw e;\n}";
            scriptActivity3.setScript(script3);
            scriptActivity3.setLanguage(ScriptActivity.LANGUAGE_JAVA);
            sequenceActivity.addChildActivity(scriptActivity1);
            sequenceActivity.addChildActivity(scriptActivity2);
            sequenceActivity.addChildActivity(scriptActivity3);
            switchActivity.addChildActivity(sequenceActivity);
            SequenceActivity sequenceActivity2 = new SequenceActivity();
            ScriptActivity scriptActivity4 = new ScriptActivity();
            String script4 = "try{\n";
            script4 += "instance.set(\"show_class_Answer" + tempCnt + "_Wrong\", new Boolean(true));\n";
            script4 += "return (new Boolean(true));\n";
            script4 += "}catch(Exception e){\n";
            script4 += "throw e;\n}";
            scriptActivity4.setScript(script);
            scriptActivity4.setLanguage(ScriptActivity.LANGUAGE_JAVA);
            ScriptActivity scriptActivity5 = new ScriptActivity();
            String script5 = "try{\n";
            script5 += "instance.set(\"show_class_Answer" + tempCnt + "_Right\", new Boolean(false));\n";
            script5 += "return (new Boolean(true));\n";
            script5 += "}catch(Exception e){\n";
            script5 += "throw e;\n}";
            scriptActivity5.setScript(script2);
            scriptActivity5.setLanguage(ScriptActivity.LANGUAGE_JAVA);
            ScriptActivity scriptActivity6 = new ScriptActivity();
            String script6 = "try{\n";
            script6 += "instance.set(\"QuestionTrue" + tempCnt + "_PV\", new Boolean(false));\n";
            script6 += "return (new Boolean(true));\n";
            script5 += "}catch(Exception e){\n";
            script5 += "throw e;\n}";
            scriptActivity6.setScript(script3);
            scriptActivity6.setLanguage(ScriptActivity.LANGUAGE_JAVA);
            sequenceActivity2.addChildActivity(scriptActivity4);
            sequenceActivity2.addChildActivity(scriptActivity5);
            sequenceActivity2.addChildActivity(scriptActivity6);
            switchActivity.addChildActivity(sequenceActivity2);
            tempCnt++;
        }
        int tempValue1 = 1;
        int tempValue2 = 1;
        for (int i = 0; i < 9; i++) {
            Or orCond = new Or();
            And andCond = new And();
            if ((i == 0) || (i == 2) || (i == 4) || (i == 6) || (i == 8)) {
                TextContext tc = new TextContext();
                tc.setText("Answer" + tempValue1);
                orCond.setDescription(tc);
                String val = new String((String) nodeValue.get("Answer" + tempValue1));
                ProcessVariable pv = (ProcessVariable) processVariable.get("Answer" + tempValue1 + "_PV");
                Evaluate evaluateCond = new Evaluate(pv, "==", val);
                ProcessVariable pv2 = (ProcessVariable) processVariable.get("QuestionTrue" + tempValue1 + "_PV");
                Boolean val2 = new Boolean(true);
                Evaluate evaluateCond2 = new Evaluate(pv2, "==", val2);
                Vector conditionVt = new Vector();
                conditionVt.add(evaluateCond);
                conditionVt.add(evaluateCond2);
                andCond.setConditionsVt(conditionVt);
                Vector andConditions = new Vector();
                andConditions.add(andCond);
                orCond.setConditionsVt(andConditions);
                tempValue1++;
            } else {
                TextContext tc = new TextContext();
                tc.setText("Answer" + tempValue2);
                orCond.setDescription(tc);
                String val = new String((String) nodeValue.get("Answer" + tempValue2));
                ProcessVariable pv = (ProcessVariable) processVariable.get("Answer" + tempValue2 + "_PV");
                Evaluate evaluateCond = new Evaluate(pv, "!=", val);
                ProcessVariable pv2 = (ProcessVariable) processVariable.get("QuestionTrue" + tempValue2 + "_PV");
                Boolean val2 = new Boolean(false);
                Evaluate evaluateCond2 = new Evaluate(pv2, "==", val2);
                Vector conditionVt = new Vector();
                conditionVt.add(evaluateCond);
                conditionVt.add(evaluateCond2);
                andCond.setConditionsVt(conditionVt);
                Vector andConditions = new Vector();
                andConditions.add(andCond);
                orCond.setConditionsVt(andConditions);
                tempValue2++;
            }
            conditions[i] = orCond;
        }
        TextContext tc = new TextContext();
        tc.setText("Otherwise");
        Otherwise otherWise = new Otherwise();
        otherWise.setDescription(tc);
        conditions[9] = otherWise;
        switchActivity.setConditions(conditions);
        eventHandler.setHandlerActivity(switchActivity);
        eventHandler.setTriggeringMethod(23);
        eventHandler.setName("CompleteActivity");
        return eventHandler;
    }
}
