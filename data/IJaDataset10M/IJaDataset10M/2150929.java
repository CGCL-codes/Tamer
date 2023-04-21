package mae.brow;

public class BlockElements extends ObjectItems {

    private String inputText;

    private Templates template;

    public BlockElements(Templates template, String inputText) {
        this.inputText = inputText;
        this.template = template;
    }

    public String toString() {
        return template.toString();
    }

    public String toCode() {
        if (inputText == null) return template.getTemplate().replace(template.hiddenText, "");
        return template.getTemplate().replace(template.hiddenText, inputText);
    }

    public enum Templates {

        IF(0), IFELSE(1), WHILE(2), DOWHILE(3), FOR(4), TRYCATCH(5), TRYFINALLY(6), TRYCATCHFINALLY(7), SYNCHRONIZED(8), RUNNABLE(9), PARENTHESIS(10);

        public final String hiddenText = "[INPUT]";

        private int ID;

        Templates(int ID) {
            this.ID = ID;
        }

        public int getID() {
            return ID;
        }

        public String getTemplate() {
            String template;
            switch(ID) {
                case 0:
                    template = "if (false) {\n\t" + hiddenText + "\n}";
                    break;
                case 1:
                    template = "if (false) {\n\t" + hiddenText + "\n} else {\n}";
                    break;
                case 2:
                    template = "while (false) {\n\t" + hiddenText + "\n}";
                    break;
                case 3:
                    template = "do {\n\t" + hiddenText + "\n} while (false);";
                    break;
                case 4:
                    template = "for (int i=0 ; i<0 ; i++ ) {\n\t" + hiddenText + "\n}";
                    break;
                case 5:
                    template = "try {\n\t" + hiddenText + "\n} catch (Exception e) {\n\te.printStackTrace();\n}";
                    break;
                case 6:
                    template = "try {\n\t" + hiddenText + "\n} finally {\n}";
                    break;
                case 7:
                    template = "try {\n\t" + hiddenText + "\n} catch (Exception e) {\n\te.printStackTrace();\n} finally {\n}";
                    break;
                case 8:
                    template = "synchronized () {\n\t" + hiddenText + "\n}";
                    break;
                case 9:
                    template = "Runnable runnable = new Runnable() {\n\tpublic void run() {\n\t\t" + hiddenText + "\n\t}\n};";
                    break;
                case 10:
                    template = "{\n\t" + hiddenText + "\n}";
                    break;
                default:
                    template = "";
            }
            return template;
        }

        public String toString() {
            String templateName;
            switch(ID) {
                case 0:
                    templateName = "if";
                    break;
                case 1:
                    templateName = "if / else";
                    break;
                case 2:
                    templateName = "while";
                    break;
                case 3:
                    templateName = "do / while";
                    break;
                case 4:
                    templateName = "for";
                    break;
                case 5:
                    templateName = "try / catch";
                    break;
                case 6:
                    templateName = "try / finally";
                    break;
                case 7:
                    templateName = "try / catch / finally";
                    break;
                case 8:
                    templateName = "Synchronized";
                    break;
                case 9:
                    templateName = "Runnable";
                    break;
                case 10:
                    templateName = "{}";
                    break;
                default:
                    templateName = "";
            }
            return templateName;
        }
    }
}
