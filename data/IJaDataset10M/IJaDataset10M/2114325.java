package org.yaoqiang.bpmn.editor.io;

import org.yaoqiang.bpmn.editor.model.GraphModel;
import org.yaoqiang.graph.io.YModelCodec;

/**
 * ModelCodec
 * 
 * @author Shi Yaoqiang(shi_yaoqiang@yahoo.com)
 */
public class ModelCodec extends YModelCodec {

    public ModelCodec() {
        super(new GraphModel());
    }
}
