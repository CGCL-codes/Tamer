package pa_SymposiumPlanner;

import java.io.*;
import java.util.*;
import jdrew.oo.util.*;
import nu.xom.*;

public class MessageGenerator {

    private Vector data;

    private int size;

    private String id;

    private String prot;

    private String sender;

    private String relationName;

    private String[] variableOrder;

    /**
	 * MessageGenerator Contructor
	 *
	 * @param Vector input - the bindings of the query
	 * @param int varSize - the number of variables in the query
	 * @param String senderIn - the sender of the message
	 * @param String idIn - the id of the message
	 * @param String protocolIn - the protocol of the message
	 * @param String rel - the relation of the query
	 */
    public MessageGenerator(Vector input, int varSize, String senderIn, String idIn, String protocolIn, String rel, String[] varOrder) {
        size = varSize;
        data = input;
        id = idIn;
        prot = protocolIn;
        sender = senderIn;
        relationName = rel;
        variableOrder = varOrder;
    }

    public MessageGenerator(Vector input, int varSize, String senderIn, String idIn, String protocolIn, String rel) {
        size = varSize;
        data = input;
        id = idIn;
        prot = protocolIn;
        sender = senderIn;
        relationName = rel;
    }

    /**
  * This method will generate the messages to be sent to the prova engine
  *
  * @return String[] - the messages to be sent back to the prova engine (could be multiple)
  */
    public String[] Messages(String query) {
        String[] messages = new String[data.size()];
        Iterator it = data.iterator();
        Object[][] vars;
        int j = 0;
        Vector<String[]> vectorElements = new Vector<String[]>();
        StringTokenizer st = new StringTokenizer(query, "<");
        String tempString = "";
        boolean pastRel = false;
        while (st.hasMoreTokens()) {
            tempString = st.nextToken();
            if (tempString.startsWith("Var") && pastRel) {
                vectorElements.add(new String[] { "Var", ("<" + tempString + "</Var>") });
            } else if (tempString.startsWith("Ind") && pastRel) {
                vectorElements.add(new String[] { "Ind", ("<" + tempString + "</Ind>") });
            } else if (tempString.startsWith("Rel")) {
                pastRel = true;
            }
        }
        while (it.hasNext()) {
            Element root = new Element("RuleML");
            root.setNamespaceURI("http://www.ruleml.org/0.91/xsd");
            root.addNamespaceDeclaration("xsi", "http://www.w3.org/2001/XMLSchema-instance");
            Attribute a3 = new Attribute("xsi:SchemaLocation", "http://www.w3.org/2001/XMLSchema-instance", "http://www.ruleml.org/0.91/xsd http://ibis.in.tum.de/research/ReactionRuleML/0.2/rr.xsd");
            root.addAttribute(a3);
            Document doc = new Document(root);
            Element message = new Element("Message");
            Attribute a1 = new Attribute("mode", "outbound");
            Attribute a2 = new Attribute("directive", "answer");
            message.addAttribute(a1);
            message.addAttribute(a2);
            root.appendChild(message);
            Element oid = new Element("oid");
            message.appendChild(oid);
            Element ind = new Element("Ind");
            oid.appendChild(ind);
            ind.insertChild(id, 0);
            Element protocol = new Element("protocol");
            message.appendChild(protocol);
            Element ind2 = new Element("Ind");
            protocol.appendChild(ind2);
            ind2.insertChild(prot, 0);
            Element send = new Element("sender");
            message.appendChild(send);
            Element ind3 = new Element("Ind");
            send.appendChild(ind3);
            ind3.insertChild(sender, 0);
            Element content = new Element("content");
            message.appendChild(content);
            Element atom = new Element("Atom");
            content.appendChild(atom);
            Element rel = new Element("Rel");
            atom.appendChild(rel);
            rel.insertChild(relationName, 0);
            vars = (Object[][]) it.next();
            String[] rev = new String[size];
            Object[][] temp = new Object[size][2];
            for (int a = 0; a < size; a++) {
                temp[a][0] = variableOrder[a];
                for (int b = 0; b < variableOrder.length; b++) {
                    if (vars[b][0].toString().contains(variableOrder[a])) {
                        temp[a][1] = vars[b][1];
                        break;
                    }
                }
            }
            vars = temp;
            for (int i = 0; i < size; i++) {
                Object k = vars[i][0];
                Object val = vars[i][1];
                String temp2 = val.toString();
                if (temp2.contains("</Var>")) {
                    String tempVar = temp2.substring(temp2.indexOf(">") + 1, temp2.lastIndexOf("<"));
                    temp2 = temp2.replace(tempVar, k.toString());
                }
                rev[i] = temp2;
            }
            int variableState = 0;
            for (int i = 0; i < vectorElements.size(); i++) {
                Builder bl = new Builder();
                StringReader sr = null;
                if (vectorElements.elementAt(i)[0].equals("Ind")) {
                    sr = new StringReader(vectorElements.elementAt(i)[1]);
                } else if (vectorElements.elementAt(i)[0].equals("Var")) {
                    sr = new StringReader(rev[variableState]);
                    variableState++;
                }
                Element newDocRoot = null;
                try {
                    Document doc2 = bl.build(sr);
                    Element docRoot = doc2.getRootElement();
                    newDocRoot = new Element(docRoot);
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
                atom.appendChild(newDocRoot);
            }
            String d = doc.toXML();
            String p1 = d.substring(0, 241);
            String p2 = d.substring(251);
            d = p1 + " " + p2;
            messages[j] = d;
            j++;
        }
        return messages;
    }

    public String[] Messages2() {
        String[] messages = new String[data.size()];
        Iterator it = data.iterator();
        Object[][] vars;
        int j = 0;
        while (it.hasNext()) {
            String messageTest = "";
            messageTest = "<RuleML xmlns=\"http://www.ruleml.org/0.91/xsd\"" + "\n" + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" + "\n" + "xsi:SchemaLocation=\"http://www.ruleml.org/0.91/xsd " + "\n" + "http://ibis.in.tum.de/research/ReactionRuleML/0.2/rr.xsd\">";
            messageTest = messageTest + "\n" + "\n" + "<Message mode=\"outbound\" directive=\"answer\">";
            messageTest = messageTest + "\n" + "\t <oid>" + "\n" + "\t\t <Ind>" + id + "</Ind>" + "\n" + "\t </oid>";
            messageTest = messageTest + "\n" + "\t<protocol>" + "\n" + "\t\t<Ind>" + "esb" + "</Ind>" + "\n" + "\t</protocol>";
            messageTest = messageTest + "\n" + "\t<sender>" + "\n" + "\t\t<Ind>" + sender + "</Ind>" + "\n" + "\t</sender>";
            messageTest = messageTest + "\n" + "\t<content>";
            messageTest = messageTest + "\n" + "\t\t<Rulebase>";
            vars = (Object[][]) it.next();
            Object[][] temp = new Object[size][2];
            for (int a = 0; a < size; a++) {
                temp[a][0] = variableOrder[a];
                for (int b = 0; b < variableOrder.length; b++) {
                    if (vars[b][0].toString().contains(variableOrder[a])) {
                        temp[a][1] = vars[b][1];
                        break;
                    }
                }
            }
            vars = temp;
            for (int i = 0; i < size; i++) {
                Object val = vars[i][1];
                String var = variableOrder[i];
                messageTest = messageTest + "\n" + "\t \t \t<Equal>" + "\n";
                messageTest = messageTest + "\t\t\t\t <Var>";
                String variableName = var.substring(0, var.length());
                messageTest = messageTest + variableName + "</Var>" + "\n";
                String binding = val.toString();
                StringTokenizer st = new StringTokenizer(binding, "\n");
                while (st.hasMoreTokens()) {
                    messageTest = messageTest + "\t\t\t" + "         " + st.nextToken() + "\n";
                }
                messageTest = messageTest + "\t\t\t</Equal>";
            }
            messageTest = messageTest + "\n" + "\t\t</Rulebase>";
            messageTest = messageTest + "\n" + "\t</content>";
            messageTest = messageTest + "\n" + "</Message>";
            messageTest = messageTest + "\n" + "</RuleML>";
            Builder bTest = new Builder();
            StringReader srTest = new StringReader(messageTest);
            try {
                messages[j] = messageTest;
            } catch (Exception e) {
                System.out.println(e.toString());
            }
            j++;
        }
        return messages;
    }

    public String finalMessage(String query) {
        String finalMessage = "";
        finalMessage = "<RuleML xmlns=\"http://www.ruleml.org/0.91/xsd\"" + "\n" + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" + "\n" + "xsi:SchemaLocation=\"http://www.ruleml.org/0.91/xsd " + "\n" + "http://ibis.in.tum.de/research/ReactionRuleML/0.2/rr.xsd\">";
        finalMessage = finalMessage + "\n" + "\n" + "<Message mode=\"outbound\" directive=\"no_further_answers\">";
        finalMessage = finalMessage + "\n" + "\t <oid>" + "\n" + "\t\t <Ind>" + id + "</Ind>" + "\n" + "\t </oid>";
        finalMessage = finalMessage + "\n" + "\t<protocol>" + "\n" + "\t\t<Ind>" + "esb" + "</Ind>" + "\n" + "\t</protocol>";
        finalMessage = finalMessage + "\n" + "\t<sender>" + "\n" + "\t\t<Ind>" + sender + "</Ind>" + "\n" + "\t</sender>";
        finalMessage = finalMessage + "\n" + "\t<content>";
        finalMessage = finalMessage + "\n" + "\t" + "<Atom>";
        finalMessage = finalMessage + "\n" + "\t" + "\t" + "<Rel>end</Rel>";
        finalMessage = finalMessage + "\n" + "\t" + "\t" + "\t" + "<Ind>messages</Ind>";
        finalMessage = finalMessage + "\n" + "\t" + "</Atom>";
        finalMessage = finalMessage + "\n" + "\t</content>";
        finalMessage = finalMessage + "\n" + "</Message>";
        finalMessage = finalMessage + "\n" + "</RuleML>";
        return finalMessage;
    }

    String[] TestMessages() {
        String[] messages = new String[data.size()];
        Iterator it = data.iterator();
        Object[][] vars;
        int j = 0;
        while (it.hasNext()) {
            String mes;
            mes = "Message mode=outbound directive=answer \n" + "Oid: " + id + "\n" + "Protocol: " + prot + "\n" + "Sender: " + sender + "\n" + "Content: " + "\n" + "Relation: " + relationName + "\n";
            vars = (Object[][]) it.next();
            for (int i = 0; i < size; i++) {
                Object k = vars[i][0];
                Object val = vars[i][1];
                mes = mes + "Variable: " + k.toString() + " Binding: " + val.toString() + "\n";
            }
            messages[j] = mes;
            j++;
        }
        return messages;
    }

    public String[] Messages3() {
        String[] messages = new String[data.size()];
        Iterator it = data.iterator();
        Object[][] vars;
        int j = 0;
        while (it.hasNext()) {
            String messageTest = "";
            messageTest = "<RuleML xmlns=\"http://www.ruleml.org/0.91/xsd\"" + "\n" + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" + "\n" + "xsi:SchemaLocation=\"http://www.ruleml.org/0.91/xsd " + "\n" + "http://ibis.in.tum.de/research/ReactionRuleML/0.2/rr.xsd\">";
            messageTest = messageTest + "\n" + "\n" + "<Message mode=\"outbound\" directive=\"answer\">";
            messageTest = messageTest + "\n" + "\t <oid>" + "\n" + "\t\t <Ind>" + id + "</Ind>" + "\n" + "\t </oid>";
            messageTest = messageTest + "\n" + "\t<protocol>" + "\n" + "\t\t<Ind>" + "esb" + "</Ind>" + "\n" + "\t</protocol>";
            messageTest = messageTest + "\n" + "\t<sender>" + "\n" + "\t\t<Ind>" + sender + "</Ind>" + "\n" + "\t</sender>";
            messageTest = messageTest + "\n" + "\t<content>";
            messageTest = messageTest + "\n" + "\t\t<Atom>";
            vars = (Object[][]) it.next();
            Object[][] temp = new Object[size][2];
            for (int a = 0; a < size; a++) {
                temp[a][0] = variableOrder[a];
                for (int b = 0; b < variableOrder.length; b++) {
                    if (vars[b][0].toString().contains(variableOrder[a])) {
                        temp[a][1] = vars[b][1];
                        break;
                    }
                }
            }
            vars = temp;
            for (int i = 0; i < size; i++) {
                boolean hasVaraibleAnswers = false;
                String tempMessage = "";
                Object val = vars[i][1];
                String var = variableOrder[i];
                tempMessage = tempMessage + "\n" + "\t \t \t<Equal>" + "\n";
                tempMessage = tempMessage + "\t\t\t\t <Var>";
                String variableName = var.substring(0, var.length());
                tempMessage = tempMessage + variableName + "</Var>" + "\n";
                String binding = val.toString();
                StringTokenizer st = new StringTokenizer(binding, "\n");
                while (st.hasMoreTokens()) {
                    String test = st.nextToken();
                    if (test.contains("</Var>")) {
                        hasVaraibleAnswers = true;
                        String tempVar = test.substring(test.indexOf(">") + 1, test.lastIndexOf("<"));
                        test = test.replace(tempVar, variableName);
                    }
                    tempMessage = tempMessage + "\t\t\t" + "         " + test + "\n";
                }
                tempMessage = tempMessage + "\t\t\t</Equal>";
                messageTest = messageTest + tempMessage;
            }
            messageTest = messageTest + "\n" + "\t\t</Atom>";
            messageTest = messageTest + "\n" + "\t</content>";
            messageTest = messageTest + "\n" + "</Message>";
            messageTest = messageTest + "\n" + "</RuleML>";
            Builder bTest = new Builder();
            StringReader srTest = new StringReader(messageTest);
            try {
                messages[j] = messageTest;
            } catch (Exception e) {
                System.out.println(e.toString());
            }
            j++;
        }
        return messages;
    }
}
