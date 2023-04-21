package com.agimatec.dbtransform;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import java.io.Serializable;

/**
 * Description: <br/>
 * User: roman.stumm <br/>
 * Date: 11.06.2007 <br/>
 * Time: 10:46:21 <br/>
 * Copyright: Agimatec GmbH
 */
@XStreamAlias("transformation")
public class DataTypeTransformation implements Serializable {

    private final DataType source;

    private final DataType target;

    public DataTypeTransformation(DataType from, DataType to) {
        source = from;
        target = to;
    }

    public DataType getSource() {
        return source;
    }

    public DataType getTarget() {
        return target;
    }
}
