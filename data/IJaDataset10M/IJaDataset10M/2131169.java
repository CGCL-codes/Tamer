package com.siemens.ct.exi;

import java.io.IOException;
import java.io.OutputStream;
import com.siemens.ct.exi.core.EXIHeaderEncoder;
import com.siemens.ct.exi.exceptions.EXIException;
import com.siemens.ct.exi.io.channel.BitEncoderChannel;

/**
 * An EXI stream is an EXI header followed by an EXI body. The EXI body carries
 * the content of the document, while the EXI header communicates the options
 * used for encoding the EXI body.
 * 
 * @author Daniel.Peintner.EXT@siemens.com
 * @author Joerg.Heuer@siemens.com
 * 
 * @version 0.8
 */
public class EXIStreamEncoder {

    protected final EXIHeaderEncoder exiHeader;

    protected final EXIBodyEncoder exiBody;

    protected final EXIFactory exiFactory;

    public EXIStreamEncoder(EXIFactory exiFactory) throws EXIException {
        this.exiFactory = exiFactory;
        exiHeader = new EXIHeaderEncoder();
        exiBody = exiFactory.createEXIBodyEncoder();
    }

    public EXIBodyEncoder encodeHeader(OutputStream os) throws EXIException, IOException {
        BitEncoderChannel headerChannel = new BitEncoderChannel(os);
        exiHeader.write(headerChannel, exiFactory);
        if (exiFactory.getCodingMode() == CodingMode.BIT_PACKED) {
            exiBody.setOutputChannel(headerChannel);
        } else {
            exiBody.setOutputStream(os);
        }
        return exiBody;
    }
}
