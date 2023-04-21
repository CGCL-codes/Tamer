package org.qtnew.data;

/**
 * Created by IntelliJ IDEA.
 * User: Malykh
 * Date: 22.12.2006
 * Time: 11:28:17
 * To change this template use File | Settings | File Templates.
 */
public interface SequentialDataConsumer {

    public void setDefinition(DataDefinition dataDef);

    public void consumeData(Datapoint datapoint);

    public void close();
}
