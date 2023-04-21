package it.southdown.avana.alignment;

public abstract class Alignment extends SequenceSet {

    private int alignmentLength;

    public Alignment(Sequence[] sequences, String name) {
        super(sequences, name);
        this.alignmentLength = sequences[0].getData().length();
        for (int i = 0; i < sequences.length; i++) {
            String seqData = sequences[i].getData();
            if (seqData.length() != alignmentLength) {
                throw new IllegalArgumentException("Sequence #" + (i + 1) + " is inconsistent (length is " + seqData.length() + " instead of " + alignmentLength + ")");
            }
        }
    }

    public int getAlignmentLength() {
        return alignmentLength;
    }

    public MasterAlignment getMasterAlignment() {
        return null;
    }

    public String getDefaultOutputFilename() {
        return getName() + ".afa";
    }

    public String getSummary() {
        StringBuffer sb = new StringBuffer();
        sb.append("# Alignment\t");
        sb.append(getName());
        sb.append("\n# Alignment length\t");
        sb.append(getAlignmentLength());
        sb.append("\n# Sequence count\t");
        sb.append(getSequenceCount());
        return sb.toString();
    }

    public String getCaption() {
        return getName() + " (" + getSequenceCount() + " sequences)";
    }
}
