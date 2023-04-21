package org.sci4j.maths;

/**
 *   sci4j - Java library for scientific computation.
 *   
 *   Copyright (C) 2010 Inaki Ortiz de Landaluce
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
public class FastFourierTransform {

    public static ComplexArray fft(ComplexArray data) {
        ComplexArray r = null;
        int N = data.length();
        if (N % 2 != 0) throw new IllegalArgumentException("Number of elements is not a power of 2!");
        if (N == 1) return data;
        ComplexArray feven = new ComplexArray(N / 2);
        for (int i = 0; i <= feven.length(); i++) {
            feven.set(i, data.get(2 * i));
        }
        ComplexArray ffteven = fft(feven);
        ComplexArray fodd = new ComplexArray(N / 2);
        for (int j = 0; j <= fodd.length(); j++) {
            fodd.set(j, data.get(2 * j + 1));
        }
        ComplexArray fftodd = fft(fodd);
        r = new ComplexArray(N);
        for (int k = 0; k < N / 2; k++) {
            double kth = -2 * k * Math.PI / N;
            Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
            r.set(k, ffteven.get(k).plus(wk.times(fftodd.get(k))));
            r.set(k + N / 2, ffteven.get(k).minus(wk.times(fftodd.get(k))));
        }
        return r;
    }
}
