package org.jscsi.target.connection.stage.fullfeature;

import java.io.IOException;
import java.security.DigestException;
import org.apache.log4j.Logger;
import org.jscsi.parser.BasicHeaderSegment;
import org.jscsi.parser.ProtocolDataUnit;
import org.jscsi.parser.exception.InternetSCSIException;
import org.jscsi.parser.scsi.SCSICommandParser;
import org.jscsi.target.Target;
import org.jscsi.target.connection.phase.TargetFullFeaturePhase;
import org.jscsi.target.scsi.cdb.ReadCapacity10Cdb;
import org.jscsi.target.scsi.cdb.ReadCapacity16Cdb;
import org.jscsi.target.scsi.cdb.ReadCapacityCdb;
import org.jscsi.target.scsi.cdb.ScsiOperationCode;
import org.jscsi.target.scsi.readCapacity.ReadCapacity10ParameterData;
import org.jscsi.target.scsi.readCapacity.ReadCapacity16ParameterData;
import org.jscsi.target.scsi.readCapacity.ReadCapacityParameterData;
import org.jscsi.target.scsi.sense.AdditionalSenseCodeAndQualifier;
import org.jscsi.target.scsi.sense.senseDataDescriptor.senseKeySpecific.FieldPointerSenseKeySpecificData;
import org.jscsi.target.settings.SettingsException;

public final class ReadCapacityStage extends TargetFullFeatureStage {

    private static final Logger LOGGER = Logger.getLogger(ReadCapacityStage.class);

    public ReadCapacityStage(final TargetFullFeaturePhase targetFullFeaturePhase) {
        super(targetFullFeaturePhase);
    }

    @Override
    public void execute(ProtocolDataUnit pdu) throws IOException, InterruptedException, InternetSCSIException, DigestException, SettingsException {
        final BasicHeaderSegment bhs = pdu.getBasicHeaderSegment();
        final SCSICommandParser parser = (SCSICommandParser) bhs.getParser();
        final ScsiOperationCode opCode = ScsiOperationCode.valueOf(parser.getCDB().get(0));
        ReadCapacityCdb cdb;
        if (opCode == ScsiOperationCode.READ_CAPACITY_10) cdb = new ReadCapacity10Cdb(parser.getCDB()); else if (opCode == ScsiOperationCode.READ_CAPACITY_16) cdb = new ReadCapacity16Cdb(parser.getCDB()); else {
            throw new InternetSCSIException("wrong SCSI Operation Code " + opCode + " in ReadCapacityStage");
        }
        if (Target.storageModule.checkBounds(cdb.getLogicalBlockAddress(), 0) != 0) {
            LOGGER.error("encountered " + cdb.getClass() + " in ReadCapacityStage with " + "LOGICAL BLOCK ADDRESS = " + cdb.getLogicalBlockAddress());
            final FieldPointerSenseKeySpecificData fp = new FieldPointerSenseKeySpecificData(true, true, false, 0, 0);
            final FieldPointerSenseKeySpecificData[] fpArray = new FieldPointerSenseKeySpecificData[] { fp };
            final ProtocolDataUnit responsePdu = createFixedFormatErrorPdu(fpArray, AdditionalSenseCodeAndQualifier.LOGICAL_BLOCK_ADDRESS_OUT_OF_RANGE, bhs.getInitiatorTaskTag(), parser.getExpectedDataTransferLength());
            connection.sendPdu(responsePdu);
            return;
        } else {
            ReadCapacityParameterData parameterData;
            if (cdb instanceof ReadCapacity10Cdb) parameterData = new ReadCapacity10ParameterData(Target.storageModule.getSizeInBlocks(), Target.storageModule.getBlockSizeInBytes()); else parameterData = new ReadCapacity16ParameterData(Target.storageModule.getSizeInBlocks(), Target.storageModule.getBlockSizeInBytes());
            sendResponse(bhs.getInitiatorTaskTag(), parser.getExpectedDataTransferLength(), parameterData);
        }
    }
}
