package net.sourceforge.nephila.node;

/**
 * A concrete implementation of the Target interface.
 * 
 * @author John de Michele
 * 
 * <BR><BR>Copyright (c) 2009, John de Michele and Project Nephila
 * <BR>All rights reserved.
 * <BR><BR>
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided
 * that the following conditions are met:<BR><BR>
 * <UL>
 * <LI>Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.<\LI>
 * <LI>Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer<BR>
 * in the documentation and/or other materials provided with the distribution.</LI>
 * <LI>Neither the name of Project Nephila nor the names of its contributors may be used to endorse or promote products derived from<BR>
 * this software without specific prior written permission.</LI>
 * <BR><BR>THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,<BR>
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL<BR>
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES<BR>
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)<BR>
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING<BR>
 * IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
public final class BasicTarget implements Target {

    private float floatWeight = 0.0F;

    private int intWeight = 0;

    private long nodeID;

    private boolean usesIntegerWeight;

    /**
	 * Instantiates a new target with a float weight.
	 * 
	 * @param weight the weight
	 * @param nodeID the target node ID
	 */
    public BasicTarget(final float weight, final long nodeID) {
        super();
        floatWeight = weight;
        this.nodeID = nodeID;
        usesIntegerWeight = false;
    }

    /**
	 * Instantiates a new target with an integer weight.
	 * 
	 * @param weight the weight
	 * @param nodeID the target node ID
	 */
    public BasicTarget(final int weight, final long nodeID) {
        super();
        intWeight = weight;
        this.nodeID = nodeID;
        usesIntegerWeight = true;
    }

    @Override
    public float getFloatWeight() {
        return floatWeight;
    }

    @Override
    public int getIntegerWeight() {
        return intWeight;
    }

    @Override
    public long getNodeID() {
        return nodeID;
    }

    @Override
    public boolean usesIntegerWeight() {
        return usesIntegerWeight;
    }
}
