package com.JPhysX;

/**
 * Copyright (c) 2007, Yuri Kravchik
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * * Neither the name of the Yuri Kravchik nor the names of its contributors
 *   may be used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
public final class NxClothFlag {

    public static final int NX_CLF_PRESSURE = (1 << 0);

    public static final int NX_CLF_STATIC = (1 << 1);

    public static final int NX_CLF_DISABLE_COLLISION = (1 << 2);

    public static final int NX_CLF_SELFCOLLISION = (1 << 3);

    public static final int NX_CLF_VISUALIZATION = (1 << 4);

    public static final int NX_CLF_GRAVITY = (1 << 5);

    public static final int NX_CLF_BENDING = (1 << 6);

    public static final int NX_CLF_BENDING_ORTHO = (1 << 7);

    public static final int NX_CLF_DAMPING = (1 << 8);

    public static final int NX_CLF_COLLISION_TWOWAY = (1 << 9);

    public static final int NX_CLF_TRIANGLE_COLLISION = (1 << 11);

    public static final int NX_CLF_TEARABLE = (1 << 12);

    public static final int NX_CLF_HARDWARE = (1 << 13);

    public static final int NX_CLF_COMDAMPING = (1 << 14);

    public static final int NX_CLF_VALIDBOUNDS = (1 << 15);

    public static final int NX_CLF_FLUID_COLLISION = (1 << 16);
}
