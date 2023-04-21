package simple.page.translate;

import simple.util.parse.ParseBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * The <code>Page</code> object is used to parse the page token. The
 * page token is the most complex token parsed from the JSP source.
 * It defines the properties of the genrated page, such as the imports
 * that should be used, and the content type of the page. For example.
 * <pre>
 *
 *    &lt;%@ page import="a,b,c,d" 
 *                contentType="text/html; charset=UTF-8" 
 *                extend="blah" 
 *                language="java" %&gt;
 *
 * </pre>
 * The above token is an example page directive. It includes information
 * regarding the imports that are to be used, as well as the content
 * type of the generated page. It also includes the runtime language.
 *
 * @author Niall Gallagher 
 */
class Page extends Token {

    /**
    * This is used to store the tokens taken for any imports used.
    */
    private ParseBuffer include;

    /**
    * This is used to extract the token used to define the extends.
    */
    private ParseBuffer extend;

    /**
    * This is used to store the charset token if has been defined.
    */
    private ParseBuffer charset;

    /**
    * This is used to store the content type of the page.
    */
    private ParseBuffer type;

    /**
    * This is used to store the runtime language of the page.
    */
    private ParseBuffer runtime;

    /**
    * This is used to collect the imports extracted from the page.
    */
    private List list;

    /**
    * Constructor for the <code>Page</code> token. This will create
    * several buffers used to store the attributes for the page
    * directive. These buffers are used to accumulate characters.
    */
    public Page() {
        this.runtime = new ParseBuffer();
        this.extend = new ParseBuffer();
        this.include = new ParseBuffer();
        this.charset = new ParseBuffer();
        this.type = new ParseBuffer();
        this.list = new ArrayList();
    }

    /**
    * Constructor for the <code>Page</code> token. This will create
    * several buffers used to store the attributes for the page
    * directive. These buffers are used to accumulate characters.
    * The token provided must be a page directive token.
    *
    * @param token this is the page token that is to be parsed
    */
    public Page(String token) {
        this();
        parse(token);
    }

    /**
    * This will add the imports, runtime language, charset, content
    * type to the document definition. The tokens are added only if 
    * they have been specified as attributes of the page token.
    *
    * @param source this is the document definition to populate
    * @param builder this is the builder driving the process
    */
    public void process(Definition source, Builder builder) {
        for (int i = 0; i < list.size(); i++) {
            String include = (String) list.get(i);
            source.addImport(include);
        }
        if (runtime.length() > 0) {
            source.setLanguage("" + runtime);
        }
        if (type.length() > 0) {
            source.setType("" + type);
        }
        if (charset.length() > 0) {
            source.setCharset("" + charset);
        }
    }

    /**
    * This will clear the page tokens so that the parse can be reused
    * by the builder. In practice this method just satisfies the
    * contract of the token so that this object is not abstract.
    */
    protected void init() {
        include.clear();
        extend.clear();
        charset.clear();
        type.clear();
        runtime.clear();
        off = 0;
    }

    /**
    * This method verifies that the token is a directive token before 
    * packing it and extracting the page attributes. The packing is
    * done to remove all whitespace from the token, this ensutes that
    * the attributes can be extracted from the source easily.
    */
    protected void parse() {
        if (skip("<%@")) {
            pack();
            page();
        }
    }

    /**
    * So that the token can be parsed in a simple manner this is used
    * to extract all white space from the token. The resulting text
    * is much easier to parse, and all have known terminal characters.
    */
    private void pack() {
        int pos = off;
        int len = 0;
        while (pos < count) {
            char ch = buf[pos++];
            if (!space(ch)) {
                buf[len++] = ch;
            }
        }
        count = len;
        off = 0;
    }

    /**
    * This will attempt to extract all attributes from the page token.
    * This ensures that there is no needed order to the attributes 
    * within the page token. Also, attributes are option in the token.
    */
    private void page() {
        if (skip("page")) {
            while (off < count) {
                content();
                imports();
                extend();
                runtime();
                off++;
            }
        }
    }

    /**
    * This will attempt to extract the "contentType" attribute from
    * the page token. This ensures that the charset and type text
    * are accumulated into the required buffers so that they can
    * be pushed into the document definition for generation.
    */
    private void content() {
        if (skip("content")) {
            type();
            charset();
        }
    }

    /**
    * This will extract the charset used for the page. Because this is
    * an optional element it checks for the ";charset=" token before
    * accumulating the bytes for the charset. The charser buffer will
    * contain the charset data should it exist when this is finished.
    */
    private void charset() {
        if (skip(";charset=")) {
            while (off < count) {
                char ch = buf[off++];
                if (terminal(ch)) {
                    break;
                }
                charset.append(ch);
            }
        }
    }

    /**
    * This will extract the content type of the page. The content type 
    * is a MIME type such as "text/html" or "text/xhtml". The terminal
    * for the content type is a quotation character or a semicolon.
    */
    private void type() {
        if (skip("type=")) {
            while (++off < count) {
                char ch = buf[off];
                if (terminal(ch)) {
                    break;
                }
                type.append(ch);
            }
        }
    }

    /**
    * This is used to extract the extends attribute. The extends token
    * defines a class that the page class extends. This is a simple
    * string terminated by either a quotation or a semicolon.
    */
    private void extend() {
        if (skip("extends=")) {
            while (++off < count) {
                char ch = buf[off];
                if (terminal(ch)) {
                    break;
                }
                extend.append(ch);
            }
        }
    }

    /**
    * This is used to extract the runtime language for the page. This
    * defines how the document definition is used to generate the source
    * and compile the resulting file. For instance Java or Groovy.
    */
    private void runtime() {
        if (skip("language=")) {
            while (++off < count) {
                char ch = buf[off];
                if (terminal(ch)) {
                    break;
                }
                runtime.append(ch);
            }
        }
    }

    /**
    * This is used to extract the imports that will be used by the 
    * page class. Imports appear in a comma seperated list, and is
    * terminated by a quotation character. Once an import is taken 
    * it is added into a list of imports.
    */
    private void imports() {
        if (skip("import=")) {
            while (++off < count) {
                char ch = buf[off];
                if (quote(ch)) {
                    break;
                } else if (skip("%>")) {
                    break;
                }
                include();
                insert();
            }
        }
    }

    /**
    * This is used to extract a single import from the comma
    * seperated list of tokens. Once a comma or a quotation has been
    * encountered the import is terminated and the token is stored.
    */
    private void include() {
        while (off < count) {
            char ch = buf[off];
            if (stop(ch)) {
                break;
            } else if (quote(ch)) {
                off--;
                break;
            } else {
                off++;
            }
            include.append(ch);
        }
    }

    /**
    * This is used to insert an import into the list of imports. This
    * makes use of the token buffer for the import, if there is data
    * within the import buffer it is added to the list and cleared.
    */
    private void insert() {
        if (include.length() > 0) {
            list.add(include.toString());
        }
        include.clear();
    }

    /**
    * A terminal is considered to be either a quotation character, 
    * which is either a single quote or a double quote, it is also a
    * comma or semicolon. The terminals are used to delimit all 
    * tokens extracted from the page token.
    *
    * @param ch this is the character to be evaluated
    *
    * @return this returns true if the character is a terminal
    */
    private boolean terminal(char ch) {
        return quote(ch) || stop(ch);
    }

    /**
    * This is used to determine when the start and end of a token
    * has been encountered. The terminals are '"' and '"', which 
    * are legal quotations within the JSP syntax.
    *
    * @param ch this is the character to be evaluated
    *
    * @return this returns true if the character is a quote
    */
    private boolean quote(char ch) {
        return ch == '"' || ch == '\'';
    }

    /**
    * This is used to determine when the start and end of a token
    * has been encountered. The terminals are "," and ";", which
    * are legal terminals within the JSP page directive.
    *
    * @param ch this is the character to be evaluated
    *
    * @return this returns true if the character is a stop
    */
    private boolean stop(char ch) {
        return ch == ',' || ch == ';';
    }
}
