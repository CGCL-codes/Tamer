package net.jibraltar.santos.command.java;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import net.jibraltar.santos.Consts;
import net.jibraltar.santos.util.FreeMarkerUtil;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.thoughtworks.qdox.JavaDocBuilder;

/**
 * Extracts the methods of a given Java source file using <a href="http://qdox.codehaus.org/">QDox</a> and puts
 * them in {@link Consts#FREEMARKER_CONTEXT} to make them available for further processing.
 * 
 * <br/>
 * <strong>Parameters:</strong>
 * <table border="1" width="100%" cellpadding="3" cellspacing="0">
 * <tr class="TableHeadingColor">
 * <th align="left">Name</th>
 * <th align="left">Mandatory</th>
 * <th align="left">Description</th>
 * <th align="left">Notes</th>
 * </tr>
 * <tr class="TableRowColor">
 * <td><code>santos.java.qdox.fileName</code></td>
 * <td>Yes</td>
 * <td>The Java source file name to be parsed</td>
 * <td>Processed as FreeMarker template before extracting the methods</td>
 * </tr>
 * <tr class="TableRowColor">
 * <td><code>santos.java.qdox.className</code></td>
 * <td>Yes</td>
 * <td>The Java class name to be parsed</td>
 * <td>Processed as FreeMarker template before extracting the methods</td>
 * </tr>
 * <tr class="TableRowColor">
 * <td><code>santos.java.qdox.modelName</code></td>
 * <td>Yes</td>
 * <td>The data model name that the methods array is bound to in the context</td>
 * <td>&nbsp;</td>
 * </tr>
 * </table>
 * 
 * <br/>
 * <strong>Result:</strong>
 * <ul>
 * <li>The methods array is added to {@link Consts#FREEMARKER_CONTEXT} as named in <code>santos.java.qdox.modelName</code>.</li>
 * </ul>
 * 
 * @author Tariq Dweik
 *
 */
public class ExtractMethodsCommand implements Command {

    /**
	 * The context key under which the Java source file name is stored.
	 */
    public static final String FILE_NAME = "santos.java.qdox.fileName";

    /**
	 * The context key under which the Java class name is stored.
	 */
    public static final String CLASS_NAME = "santos.java.qdox.className";

    /**
	 * The context key under which the model name is stored.
	 */
    public static final String MODEL_NAME = "santos.java.qdox.modelName";

    public boolean execute(Context context) throws Exception {
        String fileName = (String) context.get(FILE_NAME);
        if (fileName == null || fileName.trim().equals("")) {
            _log.error("Parameter " + FILE_NAME + " must be provided.");
            throw new IllegalArgumentException("Parameter " + FILE_NAME + " must be provided.");
        }
        String className = (String) context.get(CLASS_NAME);
        if (className == null || className.trim().equals("")) {
            _log.error("Parameter " + CLASS_NAME + " must be provided.");
            throw new IllegalArgumentException("Parameter " + CLASS_NAME + " must be provided.");
        }
        String modelName = (String) context.get(MODEL_NAME);
        if (modelName == null || modelName.trim().equals("")) {
            _log.error("Parameter " + MODEL_NAME + " must be provided.");
            throw new IllegalArgumentException("Parameter " + MODEL_NAME + " must be provided.");
        }
        Map root = new HashMap();
        root.putAll((Map) context.get(Consts.FREEMARKER_CONTEXT));
        root.put(context.get(Consts.MODEL_ROOT_NAME), context.get(Consts.MODEL));
        FreeMarkerUtil.copyContextParameters(context, root);
        String outputDir = (String) context.get(Consts.OUTPUT_DIR);
        fileName = outputDir + File.separator + FreeMarkerUtil.processString(fileName, root);
        className = FreeMarkerUtil.processString(className, root);
        JavaDocBuilder builder = new JavaDocBuilder();
        builder.addSource(new File(fileName));
        ((Map) context.get(Consts.FREEMARKER_CONTEXT)).put(modelName, builder.getClassByName(className).getMethods());
        return CONTINUE_PROCESSING;
    }

    private static Log _log = LogFactory.getLog(ExtractMethodsCommand.class);
}
