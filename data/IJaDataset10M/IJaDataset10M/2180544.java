package com.koutra.dist.proc.pipeline.script;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import org.apache.log4j.Logger;
import org.xml.sax.ContentHandler;
import com.koutra.dist.proc.util.EngineHelper;

/**
 * Concrete implementation of a ReaderToXMLPipelineItem that uses an
 * underlying script.
 * 
 * @author Pafsanias Ftakas
 */
public class ReaderToXMLPipelineItem extends com.koutra.dist.proc.pipeline.ReaderToXMLPipelineItem {

    private static final Logger logger = Logger.getLogger(ReaderToXMLPipelineItem.class);

    protected String scriptEngineName;

    protected String scriptPath;

    /**
	 * @deprecated use one of the initializing constructors
	 */
    public ReaderToXMLPipelineItem() {
    }

    /**
	 * Initializing constructor.
	 * @param scriptEngineName the script engine name to use.
	 * @param scriptPath the path to the script file to use.
	 */
    public ReaderToXMLPipelineItem(String id, String scriptEngineName, String scriptPath) {
        super(id);
        this.scriptEngineName = scriptEngineName;
        this.scriptPath = scriptPath;
    }

    /**
	 * Implementation of the IReaderSAXCallback that uses the script as an
	 * underlying implementation.
	 */
    @Override
    public void transformBuffer(char[] buffer, int offset, int length, ContentHandler contentHandler) {
        try {
            ScriptEngine scriptEngine = EngineHelper.getInstance().evalScript(scriptEngineName, scriptPath);
            Invocable invocableEngine = (Invocable) scriptEngine;
            invocableEngine.invokeFunction("transformBuffer", buffer, offset, length, contentHandler);
        } catch (ScriptException e) {
            logger.error("Error invoking function 'transformBuffer'", e);
        } catch (NoSuchMethodException e) {
            logger.error("Error invoking function 'transformBuffer'", e);
        } catch (FileNotFoundException e) {
            logger.error("Error invoking function 'transformBuffer'", e);
        }
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
