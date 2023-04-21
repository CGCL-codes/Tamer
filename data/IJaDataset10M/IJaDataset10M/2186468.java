package uk.ac.roslin.ensembl.biojava3;

import java.io.Serializable;
import org.biojava3.core.sequence.compound.NucleotideCompound;
import org.biojava3.core.sequence.template.ProxySequenceReader;

/**
 *
 * @author paterson
 */
public interface EnsemblDNASequenceReader extends ProxySequenceReader<NucleotideCompound>, Serializable {

    public String getName();

    public void setName(String name);

    public Integer getCoordSystemID();

    public void setCoordSystemID(Integer coordSystemID);

    public Integer getLengthInteger();

    public void setLength(Integer length);

    public Integer getSeqRegionID();

    public void setSeqRegionID(Integer seqRegionID);

    public String getReverseComplementSequenceAsString(Integer start, Integer end);
}
