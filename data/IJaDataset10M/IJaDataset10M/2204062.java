package com.siemens.ct.exi.io.block;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.siemens.ct.exi.core.CompileConfiguration;
import com.siemens.ct.exi.datatype.encoder.TypeEncoder;
import com.siemens.ct.exi.io.channel.ByteEncoderChannelChannelized;
import com.siemens.ct.exi.io.channel.EncoderChannel;
import com.siemens.ct.exi.io.channel.EncoderChannelChannelized;

/**
 * TODO Description
 * 
 * @author Daniel.Peintner.EXT@siemens.com
 * @author Joerg.Heuer@siemens.com
 * 
 * @version 0.3.20080718
 */
public abstract class AbstractEncoderByteBlockChannelized extends AbstractEncoderBlock {

    protected EncoderChannelChannelized structureChannel;

    protected Map<String, Map<String, EncoderChannelChannelized>> valueChannels;

    protected List<String> orderValueChannelUri;

    protected List<String> orderValueChannelLocalName;

    public AbstractEncoderByteBlockChannelized(OutputStream outputStream, TypeEncoder typeEncoder) {
        super(outputStream, typeEncoder);
    }

    protected void init() {
        structureChannel = createEncoderChannel();
        valueChannels = new HashMap<String, Map<String, EncoderChannelChannelized>>();
        orderValueChannelUri = new ArrayList<String>();
        orderValueChannelLocalName = new ArrayList<String>();
    }

    protected void addNewExpandedNameChannel(String uri, String localName) {
        orderValueChannelUri.add(uri);
        orderValueChannelLocalName.add(localName);
    }

    private static EncoderChannelChannelized createEncoderChannel() {
        return new ByteEncoderChannelChannelized();
    }

    protected EncoderChannel getStructureChannel() {
        return structureChannel;
    }

    protected EncoderChannel getValueChannel(String uri, String localName) {
        if (valueChannels.containsKey(uri)) {
            Map<String, EncoderChannelChannelized> valueChannelsLocalNamePerURI = valueChannels.get(uri);
            if (!valueChannelsLocalNamePerURI.containsKey(localName)) {
                valueChannelsLocalNamePerURI.put(localName, createEncoderChannel());
                addNewExpandedNameChannel(uri, localName);
            }
            return valueChannelsLocalNamePerURI.get(localName);
        } else {
            HashMap<String, EncoderChannelChannelized> valueChannelsLocalNamePerURI = new HashMap<String, EncoderChannelChannelized>();
            valueChannelsLocalNamePerURI.put(localName, createEncoderChannel());
            valueChannels.put(uri, valueChannelsLocalNamePerURI);
            addNewExpandedNameChannel(uri, localName);
            return valueChannelsLocalNamePerURI.get(localName);
        }
    }

    protected abstract OutputStream getStream() throws IOException;

    protected abstract void finalizeStream() throws IOException;

    public void flush() throws IOException {
        assert (orderValueChannelUri.size() == orderValueChannelLocalName.size());
        if (getNumberOfBlockValues() <= CompileConfiguration.MAX_NUMBER_OF_VALUES) {
            OutputStream singleStream = getStream();
            singleStream.write(structureChannel.toByteArray());
            for (int i = 0; i < orderValueChannelUri.size(); i++) {
                singleStream.write(valueChannels.get(orderValueChannelUri.get(i)).get(orderValueChannelLocalName.get(i)).toByteArray());
            }
            finalizeStream();
        } else {
            OutputStream structureStream = this.getStream();
            structureStream.write(structureChannel.toByteArray());
            finalizeStream();
            OutputStream lessEq100Stream = this.getStream();
            boolean wasThereLess100 = false;
            for (int i = 0; i < orderValueChannelUri.size(); i++) {
                EncoderChannelChannelized channelLEQ100 = valueChannels.get(orderValueChannelUri.get(i)).get(orderValueChannelLocalName.get(i));
                if (channelLEQ100.getNumberOfChannelValues() <= CompileConfiguration.MAX_NUMBER_OF_VALUES) {
                    lessEq100Stream.write(channelLEQ100.toByteArray());
                    wasThereLess100 = true;
                }
            }
            if (wasThereLess100) {
                finalizeStream();
            }
            for (int i = 0; i < orderValueChannelUri.size(); i++) {
                EncoderChannelChannelized channelGre100 = valueChannels.get(orderValueChannelUri.get(i)).get(orderValueChannelLocalName.get(i));
                if (channelGre100.getNumberOfChannelValues() > CompileConfiguration.MAX_NUMBER_OF_VALUES) {
                    OutputStream great100Stream = this.getStream();
                    great100Stream.write(channelGre100.toByteArray());
                    finalizeStream();
                }
            }
        }
        this.outputStream.flush();
    }
}
