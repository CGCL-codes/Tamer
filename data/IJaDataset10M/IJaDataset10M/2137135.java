package com.koutra.dist.proc.pipeline.script;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import org.apache.log4j.Logger;
import com.koutra.dist.proc.util.EngineHelper;

/**
 * Concrete implementation of a LineBasedReaderToWriterPipelineItem that uses an
 * underlying script.
 * 
 * @author Pafsanias Ftakas
 */
public class LineBasedReaderToWriterPipelineItem extends com.koutra.dist.proc.pipeline.LineBasedReaderToWriterPipelineItem {

    private static final Logger logger = Logger.getLogger(LineBasedReaderToWriterPipelineItem.class);

    protected String scriptEngineName;

    protected String scriptPath;

    /**
	 * @deprecated use one of the initializing constructors
	 */
    public LineBasedReaderToWriterPipelineItem() {
    }

    /**
	 * Initializing constructor.
	 * @param scriptEngineName the script engine name to use.
	 * @param scriptPath the path to the script file to use.
	 */
    public LineBasedReaderToWriterPipelineItem(String id, String scriptEngineName, String scriptPath) {
        super(id);
        this.scriptEngineName = scriptEngineName;
        this.scriptPath = scriptPath;
    }

    /**
	 * Implementation of the LineBasedReaderToWriterPipelineItem that uses the script as an
	 * underlying implementation.
	 */
    @Override
    public String transformLine(String line) {
        try {
            ScriptEngine scriptEngine = EngineHelper.getInstance().evalScript(scriptEngineName, scriptPath);
            Invocable invocableEngine = (Invocable) scriptEngine;
            return (String) invocableEngine.invokeFunction("transformLine", line);
        } catch (ScriptException e) {
            logger.error("Error invoking function 'transformLine'", e);
        } catch (NoSuchMethodException e) {
            logger.error("Error invoking function 'transformLine'", e);
        } catch (FileNotFoundException e) {
            logger.error("Error invoking function 'transformLine'", e);
        }
        return "";
    }

    /**
	 * Override the <code>Streamable</code> implementation in order to deserialize
	 * local members.
	 */
    @Override
    public void readFrom(DataInputStream in) throws IOException, IllegalAccessException, InstantiationException {
        super.readFrom(in);
        scriptEngineName = in.readUTF();
        scriptPath = in.readUTF();
    }

    /**
	 * Override the <code>Streamable</code> implementation in order to serialize
	 * local members.
	 */
    @Override
    public void writeTo(DataOutputStream out) throws IOException {
        super.writeTo(out);
        out.writeUTF(scriptEngineName);
        out.writeUTF(scriptPath);
    }
}
