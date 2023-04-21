package simpleorm.simplets.plain;

import simpleorm.simplets.servlet.HEmptyRequestlet;

/**
 * Methods to output <INPUT> etc.
 * @author aberglas
 */
public class HHtmlInput {

    HPlainRequestlet sreq;

    HHtmlBasic hb;

    HHtmlInput(HPlainRequestlet sreq) {
        this.sreq = sreq;
        hb = sreq.hb;
    }

    private void outHtml(String html) {
        sreq.outHtml(html);
    }

    private void pushHtml(String html) {
        sreq.pushHtml(html);
    }

    private void popHtml(String html) {
        sreq.popHtml(html);
    }

    private void endTag(String extras) {
        if (extras != null) {
            outHtml(" ");
            outHtml(extras);
        }
        outHtml(">\n");
    }

    /** &lt;INPUT NAME=name SIZE=size VALUE=request.name extras> */
    public void input(String name, int length, String extras) {
        outHtml("<INPUT name='");
        sreq.outEscaped(name);
        outHtml("' id='");
        sreq.outEscaped(name);
        outHtml("' size=" + length + " value='");
        sreq.outEscaped(sreq.paramattr(name, ""));
        outHtml("'");
        endTag(extras);
    }

    public void input(String name, int length) {
        input(name, length, null);
    }

    /** &lt;INPUT TYPE=CHECKBOX NAME=name CHECKED=CHECKED if request.name != null> <p>
	   *  Note that if there are multiple checkboxes they should each be given a unique name, not a value.
	   */
    public void checkbox(String name, String extras) {
        outHtml("<INPUT type=checkbox name='");
        sreq.outEscaped(name);
        outHtml("' id='");
        sreq.outEscaped(name);
        outHtml("'");
        if (sreq.paramattr(name) != null) outHtml(" checked=checked ");
        endTag(extras);
    }

    public void checkbox(String name) {
        checkbox(name, null);
    }

    /** &lt;INPUT TYPE=RADIO NAME=name CHECKED=CHECKED if request.name == valuel> <p>
	   *  Multiple radio buttons can be given the same name as only one can be checked.
	   *  Value must not be null.  id = name + '__' + value, must be unique for each button.
	   */
    public void radio(String name, String value, String extras) {
        outHtml("<INPUT type=radio name='");
        sreq.outEscaped(name);
        outHtml("' id='");
        sreq.outEscaped(name + "__" + value);
        outHtml("' value= '");
        sreq.outEscaped(value);
        outHtml("' ");
        if (value.equals(sreq.paramattr(name))) outHtml(" checked=checked ");
        endTag(extras);
    }

    public void radio(String name, String value) {
        radio(name, value, null);
    }

    /** select with escaped options. */
    public void select(String name, String extras, String[] options) {
        pushHtml("<SELECT name='");
        sreq.outEscaped(name);
        outHtml("'");
        endTag(extras);
        for (int ox = 0; ox < options.length + 1; ox++) {
            String option = ox == 0 ? "" : options[ox - 1];
            pushHtml("  <OPTION value='");
            sreq.outEscaped(option);
            outHtml("'");
            if (option != null && option.equals(sreq.paramattr(name))) outHtml(" selected=selected");
            outHtml(">\n");
            sreq.outEscaped(option);
            popHtml("</OPTION>\n");
        }
        popHtml("</SELECT>\n");
    }

    /** select with combo option. */
    public void selectCombo(String name, String extras, String[] options) {
        select(name, extras, options);
        outHtml("&nbsp;");
        input(name, 20, extras);
    }

    /** Hidden input, to preserve state.  NOT Encrypted or signed. */
    public void hidden(String name, String value) {
        outHtml("<INPUT type=hidden name='");
        sreq.outEscaped(name);
        outHtml("' id='");
        sreq.outEscaped(name);
        outHtml("' value='");
        sreq.outEscaped(value);
        outHtml("'>\n");
    }

    /** &lt;INPUT TYPE=SUBMIT NAME=name CHECKED=CHECKED if request.name == valuel> <p>
	   *  Multiple radio buttons can be given the same name as only one can be checked.
	   *  Value must not be null.  Name + value must be unique for each button.
	   */
    public void submit(String name, String value, String extras) {
        outHtml("<INPUT type=submit name='");
        sreq.outEscaped(name);
        outHtml("' value= '");
        sreq.outEscaped(value);
        outHtml("' ");
        endTag(extras);
    }

    /** Default name is "f.submit" */
    public void submit(String value) {
        submit("f.submit", value, null);
    }

    /** &ltFORM>
	   * Always posts back to current page.
	   */
    public void form() {
        pushHtml("<FORM>\n");
    }

    public void _form() {
        popHtml("</FORM>\n");
    }

    /** Fixed size prompt for a field (uses a float).  
	   *  name used to set label for=, good for disabilities?
	   */
    public void fieldPrompt(String prompt, String inputName) {
        outHtml("<label for='");
        sreq.outEscaped(inputName);
        outHtml("'>");
        hb.prompt(prompt);
        outHtml("</label>");
    }

    /** Prompt defaults to input name. */
    public void fieldPrompt(String inputName) {
        fieldPrompt(inputName, inputName);
    }

    /**
	   * Prompt + Input field. spaced out on a line.
	   * For more complex UIs use the building blocks!.
	   */
    public void field(String inputName, int length, String prompt) {
        fieldPrompt(prompt, inputName);
        input(inputName, length, "style='margin:1'");
        outHtml(" ");
        hb.outputFieldErrorMessages(inputName);
        outHtml("<br>\n");
    }

    /** Prompt defaults to inputName, the normal case.
	    * (input name is used for error messages etc.)
	    */
    public void field(String inputName, int length) {
        field(inputName, length, inputName);
    }
}
