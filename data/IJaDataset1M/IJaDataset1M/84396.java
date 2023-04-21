package org.tigr.seq.asmbr;

import java.util.*;
import org.tigr.cloe.utils.Range;
import org.tigr.seq.log.*;
import org.tigr.seq.seqdata.*;

/**
 *
 * An unremarkable and generic implementation of the
 * <code>ICoverageMap</code> interface.  Theoretically this should
 * support any result assembly, not just result assemblies that
 * represent closed gaps.
 *
 * <p>
 * Copyright &copy; 2002 The Institute for Genomic Research (TIGR).
 * <p>
 * All rights reserved.
 * 
 * <pre>
 * $RCSfile: DefaultCoverageMap.java,v $
 * $Revision: 1.27 $
 * $Date: 2005/12/09 18:27:28 $
 * $Author: dkatzel $
 * </pre>
 * 
 *
 * @author Miguel Covarrubias
 * @version 1.0 */
public class DefaultCoverageMap implements ICoverageMap {

    /**
     * The result assembly for which this coverage map was generated.
     *
     * */
    private IBaseAssembly referenceAssembly;

    /**
     * The list of <code>ICoverageMapVertex</code>es comprising this
     * coverage map.
     *
     * */
    private ArrayList<ICoverageMapVertex> vertexList;

    private ArrayList<IBaseAssemblySequence> coveringSequences = new ArrayList<IBaseAssemblySequence>();

    /**
     * The left coordinate of the core coverage region, the region
     * defined by the assembly context length of the former gapward
     * end of the input flanking assembly.  
     *
     * */
    private int leftCoreCoordinate = ICoverageMap.NO_SUCH_POSITION;

    /**
     * The right coordinate of the core coverage region, the region
     * defined by the assembly context length of the former gapward
     * end of the input flanking assembly.  
     *
     * */
    private int rightCoreCoordinate = ICoverageMap.NO_SUCH_POSITION;

    /**
     * The maximum coverage in the core coverage region.
     *
     *
     */
    private int maximumCoreCoverage;

    /**
     * The minimum coverage in the core coverage region.
     *
     *
     */
    private int minimumCoreCoverage;

    /**
     * Creates a new <code>DefaultCoverageMap</code> instance.
     *
     *
     * @param pAssembly an <code>IResultAssembly</code> value
     * 
     * @exception SeqdataException if an error occurs
     *
     */
    public DefaultCoverageMap(IBaseAssembly pAssembly) throws SeqdataException {
        if (pAssembly == null) throw new NullPointerException();
        this.vertexList = new ArrayList<ICoverageMapVertex>();
        this.referenceAssembly = pAssembly;
        try {
            this.buildVertexList();
        } catch (SeqdataException sx) {
            Log.log(Log.ERROR, new Throwable(), sx, ResourceUtil.getMessage(DefaultCoverageMap.class, "caught_seqdata_exception"));
            throw new SeqdataException(sx.getMessage());
        }
    }

    /**
     * Helper method for building the vertex list that is a product of
     * merging collections of result assembly sequences sorted by left
     * and right assembly ends.
     *
     *
     * @param pCoveringSequences a <code>Map</code> value
     * 
     * @param pRightIterator an <code>Iterator</code> value
     * 
     * @return an <code>IBaseAssemblySequence</code> value
     *
     * @exception SeqdataException if an error occurs
     * */
    private IBaseAssemblySequence takeLeavingSequence(ArrayList<IBaseAssemblySequence> pCoveringSequences, IBaseAssemblySequence pLastRight, Iterator pRightIterator) throws SeqdataException {
        IBaseAssemblySequence ret = null;
        pCoveringSequences.remove(pLastRight);
        List<IBaseAssemblySequence> thisList = new ArrayList<IBaseAssemblySequence>();
        thisList.add(pLastRight);
        List<IBaseAssemblySequence> emptyList = new ArrayList<IBaseAssemblySequence>(0);
        ICoverageMapVertex vertex = new DefaultCoverageMapVertex(pLastRight.getEndOffset() + 1, new ArrayList<IBaseAssemblySequence>(pCoveringSequences), emptyList, thisList);
        this.vertexList.add(vertex);
        if (pRightIterator.hasNext()) {
            ret = (IBaseAssemblySequence) pRightIterator.next();
        }
        return ret;
    }

    /**
     * Helper method for building the vertex list that is a product of
     * merging collections of result assembly sequences sorted by left
     * and right assembly ends.
     *
     *
     * @param pCoveringSequences a <code>Map</code> value
     * 
     * @param pLeftIterator an <code>Iterator</code> value
     * 
     * @return an <code>IBaseAssemblySequence</code> value
     *
     * @exception SeqdataException if an error occurs
     * */
    private IBaseAssemblySequence takeEnteringSequence(ArrayList<IBaseAssemblySequence> pCoveringSequences, IBaseAssemblySequence pLastLeft, Iterator pLeftIterator) throws SeqdataException {
        IBaseAssemblySequence ret = null;
        pCoveringSequences.add(pLastLeft);
        List<IBaseAssemblySequence> thisList = new ArrayList<IBaseAssemblySequence>();
        thisList.add(pLastLeft);
        List<IBaseAssemblySequence> emptyList = new ArrayList<IBaseAssemblySequence>();
        ICoverageMapVertex vertex = new DefaultCoverageMapVertex(pLastLeft.getStartOffset(), new ArrayList<IBaseAssemblySequence>(pCoveringSequences), thisList, emptyList);
        this.vertexList.add(vertex);
        if (pLeftIterator.hasNext()) {
            ret = (IBaseAssemblySequence) pLeftIterator.next();
        }
        return ret;
    }

    /**
     *
     * Iterate over the sequences providing coverage to the result
     * assembly and generate our List of vertices. 
     *
     * @exception SeqdataException if an error occurs
     * */
    private void buildVertexList() throws SeqdataException {
        ArrayList<IBaseAssemblySequence> allSequences = new ArrayList<IBaseAssemblySequence>(this.referenceAssembly.getContainedSequences());
        this.coveringSequences = new ArrayList<IBaseAssemblySequence>(allSequences);
        Iterator allIter = allSequences.iterator();
        while (allIter.hasNext()) {
            IBaseAssemblySequence seq = (IBaseAssemblySequence) allIter.next();
            if (seq.isPseudoRead()) {
                this.coveringSequences.remove(seq);
            }
        }
        allSequences = null;
        Collection<IBaseAssemblySequence> sequences = this.coveringSequences;
        Comparator leftComparator = new Comparator() {

            public int compare(Object thiz, Object that) {
                int left = 0;
                int right = 0;
                IBaseAssemblySequence thisSeq;
                IBaseAssemblySequence thatSeq;
                int ret = 0;
                try {
                    thisSeq = (IBaseAssemblySequence) thiz;
                    thatSeq = (IBaseAssemblySequence) that;
                    left = thisSeq.getStartOffset();
                    right = thatSeq.getStartOffset();
                    ret = left - right;
                    if (ret == 0) {
                        ret = thisSeq.getDelegateSequence().getSequenceID().compareTo(thatSeq.getDelegateSequence().getSequenceID());
                    }
                } catch (SeqdataException sx) {
                    throw new RuntimeException();
                }
                return ret;
            }

            public boolean equals(Object that) {
                return this == that;
            }
        };
        Comparator rightComparator = new Comparator() {

            public int compare(Object thiz, Object that) {
                int left = 0;
                int right = 0;
                IBaseAssemblySequence thisSeq;
                IBaseAssemblySequence thatSeq;
                int ret = 0;
                try {
                    thisSeq = (IBaseAssemblySequence) thiz;
                    thatSeq = (IBaseAssemblySequence) that;
                    left = thisSeq.getEndOffset();
                    right = thatSeq.getEndOffset();
                    ret = left - right;
                    if (ret == 0) {
                        ret = thisSeq.getDelegateSequence().getSequenceID().compareTo(thatSeq.getDelegateSequence().getSequenceID());
                    }
                } catch (SeqdataException sx) {
                    throw new RuntimeException();
                }
                return ret;
            }

            public boolean equals(Object that) {
                return this == that;
            }
        };
        SortedSet<IBaseAssemblySequence> leftSet = new TreeSet<IBaseAssemblySequence>(leftComparator);
        leftSet.addAll(sequences);
        SortedSet<IBaseAssemblySequence> rightSet = new TreeSet<IBaseAssemblySequence>(rightComparator);
        rightSet.addAll(sequences);
        Iterator iterLeft = leftSet.iterator();
        Iterator iterRight = rightSet.iterator();
        IBaseAssemblySequence lastLeft = (IBaseAssemblySequence) iterLeft.next();
        IBaseAssemblySequence lastRight = (IBaseAssemblySequence) iterRight.next();
        ArrayList<IBaseAssemblySequence> covSeqs = new ArrayList<IBaseAssemblySequence>();
        List<IBaseAssemblySequence> emptyList = new ArrayList<IBaseAssemblySequence>();
        while (lastLeft != null || lastRight != null) {
            if (lastLeft != null) {
                if (lastLeft.getStartOffset() <= lastRight.getEndOffset() + 1) {
                    lastLeft = this.takeEnteringSequence(covSeqs, lastLeft, iterLeft);
                } else {
                    lastRight = this.takeLeavingSequence(covSeqs, lastRight, iterRight);
                }
            } else {
                lastRight = this.takeLeavingSequence(covSeqs, lastRight, iterRight);
            }
        }
        this.compactAdjacentVertices();
        ICoverageMapVertex vertex;
        if (this.vertexList.size() > 0) {
            ICoverageMapVertex initial = (ICoverageMapVertex) this.vertexList.get(0);
            if (initial.getStartCoordinate() > 0) {
                System.out.println("DETECTED 5' NUU, adding to coverage map explicitly");
                DefaultCoverageMapVertex newInitial = new DefaultCoverageMapVertex(0, emptyList, emptyList, emptyList);
                this.vertexList.add(0, newInitial);
            }
        } else {
            System.out.println("DETECTED ALL-NUU asm, adding to coverage map explicitly");
            DefaultCoverageMapVertex newOnly = new DefaultCoverageMapVertex(0, emptyList, emptyList, emptyList);
            newOnly.setEndCoordinate(this.getAsmGappedSize() - 1);
            this.vertexList.add(newOnly);
        }
        this.setEndCoordinates();
        vertex = (ICoverageMapVertex) this.vertexList.get(this.vertexList.size() - 1);
        if (vertex.getStartCoordinate() >= this.getAsmGappedSize()) {
            this.vertexList.remove(vertex);
        }
        vertex = (ICoverageMapVertex) this.vertexList.get(this.vertexList.size() - 1);
        if (vertex.getEndCoordinate() < this.getAsmGappedSize() - 1) {
            System.out.println("DETECTED 3' NUU, adding explicitly...");
            DefaultCoverageMapVertex newTerminal = new DefaultCoverageMapVertex(vertex.getEndCoordinate() + 1, emptyList, emptyList, emptyList);
            this.vertexList.add(newTerminal);
        }
        this.setStrandedness();
    }

    /**
     * Compact adjacent vertices at the same coordinate to a single
     * vertex containing merged entering, leaving, and supporting data
     *
     *
     * @exception SeqdataException if an error occurs
     * */
    private void compactAdjacentVertices() throws SeqdataException {
        Iterator iter = this.vertexList.iterator();
        if (iter.hasNext()) {
            ICoverageMapVertex lastVertex = (ICoverageMapVertex) iter.next();
            ArrayList<ICoverageMapVertex> clonedVertexList = new ArrayList<ICoverageMapVertex>(vertexList);
            while (iter.hasNext()) {
                ICoverageMapVertex vertex = (ICoverageMapVertex) iter.next();
                if (vertex != null) {
                    if (lastVertex.getStartCoordinate() == vertex.getStartCoordinate()) {
                        lastVertex.getEnteringSequences().addAll(vertex.getEnteringSequences());
                        lastVertex.getLeavingSequences().addAll(vertex.getLeavingSequences());
                        lastVertex.getSupportingSequences().removeAll(vertex.getLeavingSequences());
                        lastVertex.getSupportingSequences().addAll(vertex.getEnteringSequences());
                        clonedVertexList.remove(vertex);
                    } else {
                        lastVertex = vertex;
                    }
                }
            }
            this.vertexList = clonedVertexList;
        }
    }

    /**
     * Set the end coordinates on our list of
     * <code>ICoverageMapVertex</code>es.
     *
     *
     * @exception SeqdataException if an error occurs
     * */
    private void setEndCoordinates() throws SeqdataException {
        try {
            Iterator iter = this.vertexList.iterator();
            DefaultCoverageMapVertex lastVertex = (DefaultCoverageMapVertex) iter.next();
            while (iter.hasNext()) {
                DefaultCoverageMapVertex vertex = (DefaultCoverageMapVertex) iter.next();
                int end = vertex.getStartCoordinate() - 1;
                lastVertex.setEndCoordinate(end);
                lastVertex = vertex;
            }
            lastVertex.setEndCoordinate(this.getAsmGappedSize() - 1);
        } catch (SeqdataException sx) {
            Log.log(Log.ERROR, new Throwable(), sx, ResourceUtil.getMessage(DefaultCoverageMap.class, "caught_seqdata_exception"));
            String message = ResourceUtil.getResource(DefaultCoverageMap.class, "text.caught_seqdata_exception", sx.getMessage());
            throw new SeqdataException(message);
        }
    }

    /**
     * Get the <code>ICoverageMapVertex</code>es comprising this
     * coverage map.
     *
     *
     * @return a <code>List</code> value
     *
     * @exception SeqdataException if an error occurs
     *
     */
    public List<ICoverageMapVertex> getVertices() throws SeqdataException {
        return this.vertexList;
    }

    /**
     * Get the assembly for which this coverage map was constructed.
     *
     *
     * @return an <code>IBaseAssembly</code> value
     *
     * @exception SeqdataException if an error occurs
     * */
    public IBaseAssembly getReferenceAssembly() throws SeqdataException {
        return this.referenceAssembly;
    }

    /**
     * Simple getter.
     *
     *
     * @return an <code>int</code> value
     *
     * @exception SeqdataException if an error occurs
     *
     */
    public int getLeftCoreCoordinate() throws SeqdataException {
        return this.leftCoreCoordinate;
    }

    /**
     * Simple getter.
     *
     *
     * @return an <code>int</code> value
     *
     * @exception SeqdataException if an error occurs
     *
     */
    public int getRightCoreCoordinate() throws SeqdataException {
        return this.rightCoreCoordinate;
    }

    /**
     * Set the core coverage region for this coverage map, patching
     * the contained vertices as well.  The core coverage region is
     * the "interesting" part of the assembly, usually the interior
     * part sufficiently far away from the low-coverage assembly ends.
     * 
     * @param pLeftCoordinate an <code>int</code> value
     * 
     * @param pRightCoordinate an <code>int</code> value
     * 
     * @exception SeqdataException if an error occurs
     * */
    public void setCoreCoverage(int pLeftCoordinate, int pRightCoordinate) throws SeqdataException {
        this.leftCoreCoordinate = pLeftCoordinate;
        this.rightCoreCoordinate = pRightCoordinate;
        Iterator iter = this.vertexList.iterator();
        int max = -1;
        int min = 999999999;
        while (iter.hasNext()) {
            ICoverageMapVertex vertex = (ICoverageMapVertex) iter.next();
            vertex.setCoreCoverage(pLeftCoordinate, pRightCoordinate);
            if (vertex.inCoreCoverageRegion()) {
                int coverage = vertex.getCoverageDepth();
                if (coverage < min) {
                    min = coverage;
                } else if (coverage > max) {
                    max = coverage;
                }
            }
        }
        this.maximumCoreCoverage = max;
        this.minimumCoreCoverage = min;
    }

    /**
     * Get the maximum coverage depth in the core coverage region.
     *
     *
     * @return an <code>int</code> value
     *
     * @exception SeqdataException if an error occurs
     *
     */
    public int getMaximumCoreCoverage() throws SeqdataException {
        return this.maximumCoreCoverage;
    }

    /**
     * Get the minimum coverage depth in the core coverage region.
     *
     *
     * @return an <code>int</code> value
     *
     * @exception SeqdataException if an error occurs
     *
     */
    public int getMinimumCoreCoverage() throws SeqdataException {
        return this.minimumCoreCoverage;
    }

    /**
     * How many vertices in this coverage map?
     *
     *
     * @return an <code>int</code> value
     *
     * @exception SeqdataException if an error occurs
     *
     */
    public int getVertexCount() throws SeqdataException {
        int ret = 0;
        if (this.vertexList != null) {
            ret = this.vertexList.size();
        }
        return ret;
    }

    /**
     * Get the vertex at the specified zero-based index into this
     * coverage map.  Useful for alternative core coverage range
     * computation.
     *
     *
     * @param pIndex an <code>int</code> value
     * 
     * @return an <code>ICoverageMapVertex</code> value
     *
     * @exception SeqdataException if an error occurs
     * */
    public ICoverageMapVertex getVertex(int pIndex) throws SeqdataException {
        if (this.vertexList == null) {
            String message = ResourceUtil.getResource(DefaultCoverageMap.class, "text.no_vertex_list_defined");
            throw new SeqdataException(message);
        }
        if (pIndex < 0 || pIndex >= this.getVertexCount()) {
            String message = ResourceUtil.getResource(DefaultCoverageMap.class, "text.invalid_vertex_index", new Integer(pIndex), new Integer(this.getVertexCount() - 1));
            throw new SeqdataException(message);
        }
        return (ICoverageMapVertex) this.vertexList.get(pIndex);
    }

    /**
     * Have each vertex determine whether it provides single or
     * dual-stranded support to the assembly
     *
     *
     * @exception SeqdataException if an error occurs
     * */
    private void setStrandedness() throws SeqdataException {
        Iterator iter = this.vertexList.iterator();
        while (iter.hasNext()) {
            DefaultCoverageMapVertex vertex = (DefaultCoverageMapVertex) iter.next();
            vertex.calculateStrandedness();
        }
    }

    public int getCoveringSequenceCount() throws SeqdataException {
        return this.coveringSequences.size();
    }

    public IBaseAssemblySequence getCoveringSequence(int i) throws SeqdataException {
        return (IBaseAssemblySequence) this.coveringSequences.get(i);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        Iterator iter = this.vertexList.iterator();
        while (iter.hasNext()) {
            ICoverageMapVertex vertex = (ICoverageMapVertex) iter.next();
            sb.append(vertex.toString());
        }
        return sb.toString();
    }

    private int getAsmGappedSize() throws SeqdataException {
        Iterator iter = this.referenceAssembly.getContainedSequences().iterator();
        int ret = -1;
        while (iter.hasNext()) {
            IBaseAssemblySequence seq = (IBaseAssemblySequence) iter.next();
            if (seq.getEndOffset() > ret) {
                ret = seq.getEndOffset();
            }
        }
        return ret + 1;
    }

    public int getCoverageDepthAtOffset(int offset) throws SeqdataException {
        ICoverageMapVertex vertex = null;
        for (ICoverageMapVertex tempVertex : this.getVertices()) {
            int start = tempVertex.getStartCoordinate();
            int end = tempVertex.getEndCoordinate();
            if (offset >= start && offset <= end) {
                vertex = tempVertex;
                break;
            }
        }
        if (vertex == null) {
            throw new SeqdataException("could not find vertex at specified offset " + offset);
        }
        return vertex.getCoverageDepth();
    }

    public List<Range> findRangesOfZeroCoverage(int start, int end) throws SeqdataException {
        System.out.println("finding regions of 0 coverage between " + start + " , " + end);
        List<Range> regionsOfZeroCoverage = new ArrayList<Range>();
        Integer startZeroCoverage = null;
        Integer endZeroCoverage = null;
        for (int i = start; i < end; i++) {
            int depth;
            try {
                depth = getCoverageDepthAtOffset(i);
            } catch (SeqdataException seqEx) {
                depth = 0;
            }
            if (depth == 0) {
                if (startZeroCoverage == null) {
                    startZeroCoverage = new Integer(i);
                }
                endZeroCoverage = new Integer(i + 1);
            } else {
                if (startZeroCoverage != null) {
                    endZeroCoverage = new Integer(i - 1);
                    System.out.println("found area of 0 coverage from [" + startZeroCoverage + ", " + endZeroCoverage + "]");
                    Range regionToCompute = new Range(startZeroCoverage, endZeroCoverage);
                    regionsOfZeroCoverage.add(regionToCompute);
                    startZeroCoverage = null;
                }
            }
        }
        if (startZeroCoverage != null) {
            endZeroCoverage = new Integer(end - 1);
            System.out.println("found area of 0 coverage from [" + startZeroCoverage + ", " + endZeroCoverage + "]");
            Range regionToCompute = new Range(startZeroCoverage, endZeroCoverage);
            regionsOfZeroCoverage.add(regionToCompute);
            startZeroCoverage = null;
        }
        return regionsOfZeroCoverage;
    }
}
