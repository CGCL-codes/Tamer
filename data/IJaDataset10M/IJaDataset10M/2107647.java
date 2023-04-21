package org.expasy.jpl.experimental.ms.lcmsms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.expasy.jpl.experimental.exceptions.JPLIncoherentSpectrumRunAssignment;
import org.expasy.jpl.experimental.ms.peaklist.JPLFragmentationSpectrum;
import org.expasy.jpl.experimental.ms.peaklist.JPLMS1LCSpectrum;
import org.expasy.jpl.utils.iterator.JPLIterable;

/**
 * A whole LC/MSMS run description, from the peaklist point of view.
 * This bean is not supposed to be "Object optimal", but rather be something efficient for computation
 * A dogma is that once the run has been built, it is not change in depth (no spectra are remove inconsistently for example:
 * * set arrays of primary type (e.g. {double[], int[]}) are for example preferred to Collection of complex data type, but we will assume that the arrays are of identical length
 * * in the same idea, we will refer on spectra MS2 to its MS1 parent through an index into the run
 * * etc. 
 * 
 * @author Alexandre Masselot
 *
 */
public class JPLRunLcmsms implements JPLIterable<JPLFragmentationSpectrum>, Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4660665982929659292L;

    private JPLExpSourceDescription source;

    private JPLExtraInformation extraInformation;

    private long id;

    private JPLChromatogramWithRef<JPLFragmentationSpectrum> chromatogramXIC = null;

    private JPLChromatogramWithRef<JPLMS1LCSpectrum> chromatogramTIC = null;

    private List<JPLFragmentationSpectrum> fragmentationSpectraList;

    private List<JPLMS1LCSpectrum> ms1SpectraList;

    private Iterator<JPLFragmentationSpectrum> fragmentationIterator;

    private ArrayList<ArrayList<Integer>> ms1SpectraChildren = new ArrayList<ArrayList<Integer>>();

    private ArrayList<ArrayList<Integer>> fragmentationSpectraParent = new ArrayList<ArrayList<Integer>>();

    public JPLExtraInformation getExtraInformation() {
        return extraInformation;
    }

    public void setExtraInformation(JPLExtraInformation extraInformation) {
        this.extraInformation = extraInformation;
    }

    public JPLRunLcmsms() {
        this.fragmentationSpectraList = new ArrayList<JPLFragmentationSpectrum>();
        this.ms1SpectraList = new ArrayList<JPLMS1LCSpectrum>();
    }

    /**
	 * add a ms1 spectrum and set the anchor accordingly
	 * WARNING: a spectrum cannot therefore not be added into 2 runs
	 * @param spectrum
	 * @return return the index for this spectrum in the run
	 */
    public void addMS1Spectrum(JPLMS1LCSpectrum spectrum) {
        int i = ms1SpectraList.size();
        ms1SpectraList.add(spectrum);
        ms1SpectraChildren.add(new ArrayList<Integer>());
        spectrum.setRunAnchor(this, i);
    }

    /**
	 * add a fragmentation spectrum and set the anchor accordingly
	 * WARNING: a spectrum cannot therefore not be added into 2 runs
	 * @param spectrum
	 * @return return the index for this spectrum in the run
	 */
    public void addFragmentationSpectrum(JPLFragmentationSpectrum spectrum) {
        int i = fragmentationSpectraList.size();
        fragmentationSpectraList.add(spectrum);
        spectrum.setRunAnchor(this, i);
    }

    public JPLIterator<JPLFragmentationSpectrum> iterator() {
        JPLIterator<JPLFragmentationSpectrum> it = new JPLIterator<JPLFragmentationSpectrum>() {

            public JPLFragmentationSpectrum next() {
                if (fragmentationIterator == null) {
                    fragmentationIterator = fragmentationSpectraList.iterator();
                }
                return fragmentationIterator.hasNext() ? fragmentationIterator.next() : null;
            }

            public boolean hasNext() {
                if (fragmentationIterator == null) {
                    fragmentationIterator = fragmentationSpectraList.iterator();
                }
                if (fragmentationIterator.hasNext()) {
                    return true;
                } else {
                    fragmentationIterator = null;
                    return false;
                }
            }
        };
        return it;
    }

    /**
	 * add a link between a parent MS1 spectrum and a fragmentation spectrum in a run 
	 * It's clear here why we do not allow for moving around and away spectra in the run list...
	 * @param ims1
	 * @param ifrag
	 */
    public void addChildrenLink(final int ims1, final int ifrag) {
        fragmentationSpectraParent.add(new ArrayList<Integer>());
        if (!ms1SpectraChildren.get(ims1).contains(new Integer(ifrag))) {
            ms1SpectraChildren.get(ims1).add(new Integer(ifrag));
            fragmentationSpectraParent.get(ifrag).add(new Integer(ims1));
        }
    }

    /**
	 * add a link between a parent MS1 spectrum and a fragmentation spectrum in a run 
	 * @param ms1Spectrum
	 * @param fragSpectrum
	 * @throws JPLIncoherentSpectrumRunAssignment
	 */
    public void addChildrenLink(final JPLMS1LCSpectrum ms1Spectrum, final JPLFragmentationSpectrum fragSpectrum) throws JPLIncoherentSpectrumRunAssignment {
        if (ms1Spectrum.getRunAnchor().getRun() != this) throw new JPLIncoherentSpectrumRunAssignment("ms1 spectrum not anchored to the current run");
        if (fragSpectrum.getRunAnchor().getRun() != this) throw new JPLIncoherentSpectrumRunAssignment("pff spectrum not anchored to the current run");
        addChildrenLink(ms1Spectrum.getRunAnchor().getIndex(), fragSpectrum.getRunAnchor().getIndex());
    }

    /**
	 * get the indices of fragmentation spectrum linked to one msLevel=1 spectrum
	 * @param ims1
	 * @return
	 */
    public ArrayList<Integer> getMS1SpectrumChildrenIndex(final int ims1) {
        return ms1SpectraChildren.get(ims1);
    }

    /**
	 * get the indices of MSlevel=1 spectrum linked to one fragmentation spectrum
	 * @param ifrag
	 * @return
	 */
    public ArrayList<Integer> getFragmentationSpectrumParentIndex(final int ifrag) {
        return fragmentationSpectraParent.get(ifrag);
    }

    /**
	 * 
	 * @return the chromtagram pointing towards fragmentation spectra
	 */
    public JPLChromatogramWithRef<JPLFragmentationSpectrum> getChromatogramXIC() {
        return chromatogramXIC;
    }

    public void setChromatogramXIC(JPLChromatogramWithRef<JPLFragmentationSpectrum> chromatogramXIC) {
        this.chromatogramXIC = chromatogramXIC;
    }

    /**
	 * 
	 * @return the chromtagram pointing towards MS1 spectra
	 */
    public JPLChromatogramWithRef<JPLMS1LCSpectrum> getChromatogramTIC() {
        return chromatogramTIC;
    }

    public void setChromatogramTIC(JPLChromatogramWithRef<JPLMS1LCSpectrum> chromatogramTIC) {
        this.chromatogramTIC = chromatogramTIC;
    }

    public JPLExpSourceDescription getSource() {
        return source;
    }

    public void setSource(JPLExpSourceDescription source) {
        this.source = source;
    }

    public List<JPLFragmentationSpectrum> getFragmentationSpectraList() {
        return fragmentationSpectraList;
    }

    public void setFragmentationSpectraList(List<JPLFragmentationSpectrum> fragmentationSpectraList) {
        this.fragmentationSpectraList = fragmentationSpectraList;
    }

    public List<JPLMS1LCSpectrum> getMs1SpectraList() {
        return ms1SpectraList;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setMs1SpectraList(List<JPLMS1LCSpectrum> ms1SpectraList) {
        this.ms1SpectraList = ms1SpectraList;
    }
}
