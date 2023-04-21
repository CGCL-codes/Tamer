package net.paoding.rose.web.instruction;

/**
 * 
 * @author 王志亮 [qieqie.wang@gmail.com]
 * 
 */
public class Text implements InstructionHelper {

    public static TextInstruction text(Object value) {
        TextInstruction instruction = new TextInstruction();
        String text = (value == null) ? "" : value.toString();
        if (text.length() > 0) {
            if (!(value instanceof CharSequence)) {
                if (value.getClass().getName().equalsIgnoreCase("JSONObject")) {
                    text = "json:" + text;
                }
            }
        }
        instruction.text(text);
        return instruction;
    }
}
