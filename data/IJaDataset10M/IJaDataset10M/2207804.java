package shellkk.qiq.jdm.task.apply;

import java.util.ArrayList;
import java.util.List;
import javax.datamining.VerificationReport;
import javax.datamining.data.PhysicalDataRecord;
import javax.datamining.task.apply.RecordApplyTask;
import shellkk.qiq.jdm.data.PhysicalDataRecordImpl;

public class RecordApplyTaskImpl extends ApplyTaskImpl implements RecordApplyTask {

    protected PhysicalDataRecordImpl inputRecord;

    protected PhysicalDataRecordImpl outputRecord;

    protected List<PhysicalDataRecordImpl> inputRecords = new ArrayList();

    protected List<PhysicalDataRecordImpl> outputRecords = new ArrayList();

    @Override
    protected RecordApplyTaskImpl create() {
        return new RecordApplyTaskImpl();
    }

    public RecordApplyTaskImpl getCopy() {
        RecordApplyTaskImpl copy = (RecordApplyTaskImpl) super.getCopy();
        copy.setInputRecord(inputRecord == null ? null : inputRecord.getCopy());
        copy.setOutputRecord(outputRecord == null ? null : outputRecord.getCopy());
        for (PhysicalDataRecordImpl input : inputRecords) {
            copy.getInputRecords().add(input.getCopy());
        }
        for (PhysicalDataRecordImpl output : outputRecords) {
            copy.getOutputRecords().add(output.getCopy());
        }
        return copy;
    }

    public PhysicalDataRecord getInputRecord() {
        return inputRecord;
    }

    public void setInputRecord(PhysicalDataRecord inputRecord) {
        this.inputRecord = (PhysicalDataRecordImpl) inputRecord;
    }

    public PhysicalDataRecord getOutputRecord() {
        return outputRecord;
    }

    public void setOutputRecord(PhysicalDataRecord outputRecord) {
        this.outputRecord = (PhysicalDataRecordImpl) outputRecord;
    }

    public List<PhysicalDataRecordImpl> getInputRecords() {
        return inputRecords;
    }

    public void setInputRecords(List<PhysicalDataRecordImpl> inputRecords) {
        this.inputRecords = inputRecords;
    }

    public List<PhysicalDataRecordImpl> getOutputRecords() {
        return outputRecords;
    }

    public void setOutputRecords(List<PhysicalDataRecordImpl> outputRecords) {
        this.outputRecords = outputRecords;
    }

    public VerificationReport verify() {
        return null;
    }
}
