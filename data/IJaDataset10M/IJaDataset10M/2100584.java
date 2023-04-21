package org.fudaa.ctulu;

/**
 * Encapsulate the LeastSquare solution a set of linear equations. For a <i>n</i>-dimensional parameter x with: - an
 * initial estimate x0 and - a set of <i>p</i> linear ``measurement'' equations A x = b - a set of <i>q</i> linear
 * ``constraint'' equations C x = d a general least-square problems is of the form:
 *
 * <pre>
 *     min || x - x0 ||_Q0&circ;2 + || A x - b ||_R&circ;2 with C x = d
 * </pre>
 *
 * with:
 *
 * <pre>
 *     || x ||_Q&circ;2 = x&circ;T Q x
 * </pre>
 *
 * and its solution is given <a href=#solving>solving</a>:
 *
 * <pre>
 *     Q x + C&circ;T l = z  with C x = d
 * </pre>
 *
 * where <i>l</i> is the Lagrange multiplier, with:
 *
 * <pre>
 *     Q  = Q0 + A&circ;T R A and z = Q0 x0 + R A&circ;T b
 * </pre>
 *
 * as used for the ``Information'' <a href=#kalman>Kalman-Bucy</a> Filter.
 *
 * @see Cholesky used for the system resolution
 */
public class LeastSquare {

    private float[] q_;

    private float[] x_;

    /**
   * Compute the LeastSquare solution of a set of linear equations.
   *
   * @param _q0 A <i>n x n</i> symmetric positive matrix stored in a float <a href=Cholesky.html#smatrix>array</a>
   *          <i>(default is 0)</i>
   * @param _x0 A <i>n</i> array for the initial estimate <i>(default is 0)</i>
   * @param _r A <i>p x p</i> symmetric positive matrix stored in a float <a href=Cholesky.html#smatrix>array</a>
   *          <i>(default is I)</i>
   * @param _a A <i>p x n</i> matrix in a <a href=Cholesky.html#matrix>p x n</a> float array <i>(default is 0)</i>
   * @param _b A <i>p</i> array <i>(default is 0)</i>
   * @param _c A <i>q x n</i> matrix in a <a href=Cholesky.html#matrix>q x n</a> float array <i>(default is 0)</i>
   * @param _d A <i>q</i> array <i>(default is 0)</i> - A simple float can be given for Q0 or R as a scalar identity
   *          matrix.
   */
    public LeastSquare(final float[] _q0, final float[] _x0, final float[] _r, final float[] _a, final float[] _b, final float[] _c, final float[] _d, final int _n, final int _p, final int _q) throws Cholesky.NonDefiniteException {
        if ((_x0 != null) && (_x0.length != _n)) {
            throw new IllegalArgumentException("x0 has a wrong dimension");
        }
        if ((_a != null) && (_a.length != _n * _p)) {
            throw new IllegalArgumentException("A has a wrong dimension");
        }
        if ((_b != null) && (_b.length != _p)) {
            throw new IllegalArgumentException("b has a wrong dimension");
        }
        if ((_c != null) && (_c.length != _n * _q)) {
            throw new IllegalArgumentException("C has a wrong dimension");
        }
        if ((_d != null) && (_d.length != _q)) {
            throw new IllegalArgumentException("d has a wrong dimension");
        }
        Cholesky.init(Math.max(_n, _q));
        init(Math.max(_p, _q), _n);
        switch(iter(_q0, (_q0 == null) ? 0 : _q0.length, _x0, (_x0 == null) ? 0 : _x0.length, _r, (_r == null) ? 0 : _r.length, _a, (_a == null) ? 0 : _a.length, _b, (_b == null) ? 0 : _b.length, _c, (_c == null) ? 0 : _c.length, _d, (_d == null) ? 0 : _d.length, new float[Math.max(_n * (_n + 1) / 2, _n * _p)], new float[_q * (_q + 1) / 2], Cholesky.lArray, k_, q_ = new float[_n * (_n + 1) / 2], x_ = new float[_n], _n, _p, _q)) {
            case -2:
                throw new IllegalArgumentException("Q0 or R have wrong dimension");
            case -1:
                throw new Cholesky.NonDefiniteException("Q is not of full rank");
            case -3:
                throw new Cholesky.NonDefiniteException("C is not of full rank");
            default:
                throw new Cholesky.NonDefiniteException("C is not of full rank");
        }
    }

    public LeastSquare(final float _q0, final float[] _x0, final float[] _r, final float[] _a, final float[] _b, final float[] _c, final float[] _d, final int _n, final int _p, final int _q) throws Cholesky.NonDefiniteException {
        this(f2a(_q0), _x0, _r, _a, _b, _c, _d, _n, _p, 0);
    }

    public LeastSquare(final float[] _q0, final float[] _x0, final float _r, final float[] _a, final float[] _b, final float[] _c, final float[] _d, final int _n, final int _p, final int _q) throws Cholesky.NonDefiniteException {
        this(_q0, _x0, f2a(_r), _a, _b, _c, _d, _n, _p, 0);
    }

    public LeastSquare(final float _q0, final float[] _x0, final float _r, final float[] _a, final float[] _b, final float[] _c, final float[] _d, final int _n, final int _p, final int _q) throws Cholesky.NonDefiniteException {
        this(f2a(_q0), _x0, f2a(_r), _a, _b, _c, _d, _n, _p, 0);
    }

    /** Compute the LeastSquare solution when no constraints. */
    public LeastSquare(final float[] _q0, final float[] _x0, final float[] _r, final float[] _a, final float[] _b, final int _n, final int _p) throws Cholesky.NonDefiniteException {
        this(_q0, _x0, _r, _a, _b, null, null, _n, _p, 0);
    }

    public LeastSquare(final float _q0, final float[] _x0, final float[] _r, final float[] _a, final float[] _b, final int _n, final int _p) throws Cholesky.NonDefiniteException {
        this(f2a(_q0), _x0, _r, _a, _b, null, null, _n, _p, 0);
    }

    public LeastSquare(final float[] _q0, final float[] _x0, final float _r, final float[] _a, final float[] _b, final int _n, final int _p) throws Cholesky.NonDefiniteException {
        this(_q0, _x0, f2a(_r), _a, _b, null, null, _n, _p, 0);
    }

    public LeastSquare(final float _q0, final float[] _x0, final float _r, final float[] _a, final float[] _b, final int _n, final int _p) throws Cholesky.NonDefiniteException {
        this(f2a(_q0), _x0, f2a(_r), _a, _b, null, null, _n, _p, 0);
    }

    /** Compute the LeastSquare solution when no initial state. */
    public LeastSquare(final float[] _r, final float[] _a, final float[] _b, final float[] _c, final float[] _d, final int _n, final int _p, final int _q) throws Cholesky.NonDefiniteException {
        this(null, null, _r, _a, _b, _c, _d, _n, _p, _q);
    }

    public LeastSquare(final float _r, final float[] _a, final float[] _b, final float[] _c, final float[] _d, final int _n, final int _p, final int _q) throws Cholesky.NonDefiniteException {
        this(null, null, f2a(_r), _a, _b, _c, _d, _n, _p, _q);
    }

    public LeastSquare(final float[] _a, final float[] _b, final float[] _c, final float[] _d, final int _n, final int _p, final int _q) throws Cholesky.NonDefiniteException {
        this(null, null, null, _a, _b, _c, _d, _n, _p, _q);
    }

    /** Compute the LeastSquare solution when no measures. */
    public LeastSquare(final float[] _q0, final float[] _x0, final float[] _c, final float[] _d, final int _n, final int _q) throws Cholesky.NonDefiniteException {
        this(_q0, _x0, null, null, null, _c, _d, _n, 0, _q);
    }

    public LeastSquare(final float _q0, final float[] _x0, final float[] _c, final float[] _d, final int _n, final int _q) throws Cholesky.NonDefiniteException {
        this(f2a(_q0), _x0, null, null, null, _c, _d, _n, 0, _q);
    }

    /** Compute the LeastSquare solution with only measures. */
    public LeastSquare(final float[] _r, final float[] _a, final float[] _b, final int _n, final int _p) throws Cholesky.NonDefiniteException {
        this(null, null, _r, _a, _b, null, null, _n, _p, 0);
    }

    public LeastSquare(final float _r, final float[] _a, final float[] _b, final int _n, final int _p) throws Cholesky.NonDefiniteException {
        this(null, null, f2a(_r), _a, _b, null, null, _n, _p, 0);
    }

    public LeastSquare(final float[] _a, final float[] _b, final int _n, final int _p) throws Cholesky.NonDefiniteException {
        this(null, null, null, _a, _b, null, null, _n, _p, 0);
    }

    private static float[] f2a(final float _f) {
        return new float[] { _f };
    }

    /**
   * Get a vector x.
   *
   * @return A <i>n</i> array containing x
   */
    public float[] getX() {
        return x_;
    }

    /**
   * Get the matrix Q.
   *
   * @return A <a href=Cholesky.html#lmatrix>n(n+1)/2</a> array containing q
   */
    public float[] getQ() {
        return q_;
    }

    int[] k_;

    int pk_;

    int nk_;

    final void init(final int _p, final int _n) {
        if ((pk_ < _p) || (nk_ != _n)) {
            pk_ = _p;
            nk_ = _n;
            k_ = new int[_p];
            for (int j = 0; j < _p; j++) {
                k_[j] = j * _n;
            }
        }
    }

    private static int iter(final float[] _q0, final int _dimQ0, final float[] _x0, final int _dimX0, final float[] _r, final int _dimR, final float[] _a, final int _dimA, final float[] _b, final int _dimb, final float[] _c, final int _dimC, final float[] _d, final int _dimd, final float[] _l, final float[] _m, final int[] _larray, final int[] _k, final float[] _qArray, final float[] _x, final int _n, final int _p, final int _q) {
        if (_dimA == 0) {
            for (int ij = 0; ij < _n * (_n + 1) / 2; ij++) {
                _qArray[ij] = 0;
            }
            for (int i = 0; i < _n; i++) {
                _x[i] = 0;
            }
        } else if (_dimR == 0) {
            int ij = 0;
            for (int j = 0; j < _n; j++) {
                for (int i = 0; i <= j; i++) {
                    _qArray[ij] = 0;
                    for (int k = 0; k < _p; k++) {
                        _qArray[ij] += _a[i + _k[k]] * _a[j + _k[k]];
                    }
                    ij++;
                }
            }
            if (_dimb == 0) {
                for (int i = 0; i < _n; i++) {
                    _x[i] = 0;
                }
            } else {
                for (int i = 0; i < _n; i++) {
                    _x[i] = 0;
                    for (int j = 0; j < _p; j++) {
                        _x[i] += _a[i + _k[j]] * _b[j];
                    }
                }
            }
        } else if (_dimR == 1) {
            int ij = 0;
            for (int j = 0; j < _n; j++) {
                for (int i = 0; i <= j; i++) {
                    _qArray[ij] = 0;
                    for (int k = 0; k < _p; k++) {
                        _qArray[ij] += _a[i + _k[k]] * _a[j + _k[k]];
                    }
                    _qArray[ij] *= _r[0];
                    ij++;
                }
            }
            if (_dimb == 0) {
                for (int i = 0; i < _n; i++) {
                    _x[i] = 0;
                }
            } else {
                for (int i = 0; i < _n; i++) {
                    _x[i] = 0;
                    for (int j = 0; j < _p; j++) {
                        _x[i] += _a[i + _k[j]] * _b[j];
                    }
                    _x[i] *= _r[0];
                }
            }
        } else if (_dimR == _p) {
            for (int i = 0; i < _n; i++) {
                for (int j = 0; j < _p; j++) {
                    final int ij = i + _k[j];
                    _l[ij] = _r[j] * _a[ij];
                }
            }
            int ij = 0;
            for (int j = 0; j < _n; j++) {
                for (int i = 0; i <= j; i++) {
                    _qArray[ij] = 0;
                    for (int k = 0; k < _p; k++) {
                        _qArray[ij] += _a[i + _k[k]] * _l[j + _k[k]];
                    }
                    ij++;
                }
            }
            if (_dimb == 0) {
                for (int i = 0; i < _n; i++) {
                    _x[i] = 0;
                }
            } else {
                for (int i = 0; i < _n; i++) {
                    _x[i] = 0;
                    for (int j = 0; j < _p; j++) {
                        _x[i] += _l[i + _k[j]] * _b[j];
                    }
                }
            }
        } else if (_dimR == _p * (_p + 1) / 2) {
            for (int i = 0; i < _n; i++) {
                for (int j = 0; j < _p; j++) {
                    final int ij = i + _k[j];
                    _l[ij] = 0;
                    for (int k = 0; k < i; k++) {
                        _l[ij] += _r[k + _larray[i]] * _a[k + _k[j]];
                    }
                    for (int k = i; k < _n; k++) {
                        _l[ij] += _r[i + _larray[k]] * _a[k + _k[j]];
                    }
                }
            }
            int ij = 0;
            for (int j = 0; j < _n; j++) {
                for (int i = 0; i <= j; i++) {
                    _qArray[ij] = 0;
                    for (int k = 0; k < _p; k++) {
                        _qArray[ij] += _a[i + _k[k]] * _l[j + _k[k]];
                    }
                    ij++;
                }
            }
            if (_dimb == 0) {
                for (int i = 0; i < _n; i++) {
                    _x[i] = 0;
                }
            } else {
                for (int i = 0; i < _n; i++) {
                    _x[i] = 0;
                    for (int j = 0; j < _p; j++) {
                        _x[i] += _l[i + _k[j]] * _b[j];
                    }
                }
            }
        } else if (_dimR == _p * _p) {
            for (int j = 0; j < _p; j++) {
                for (int i = 0; i < _n; i++) {
                    final int ij = i + _k[j];
                    _l[ij] = 0;
                    for (int k = 0; k < _p; k++) {
                        _l[ij] += _r[j + _k[k]] * _a[k + _k[i]];
                    }
                }
            }
            int ij = 0;
            for (int j = 0; j < _n; j++) {
                for (int i = 0; i <= j; i++) {
                    _qArray[ij] = 0;
                    for (int k = 0; k < _p; k++) {
                        _qArray[ij] += _a[i + _k[k]] * _l[j + _k[k]];
                    }
                    ij++;
                }
            }
            if (_dimb == 0) {
                for (int i = 0; i < _n; i++) {
                    _x[i] = 0;
                }
            } else {
                for (int i = 0; i < _n; i++) {
                    _x[i] = 0;
                    for (int j = 0; j < _p; j++) {
                        _x[i] += _l[i + _k[j]] * _b[j];
                    }
                }
            }
        } else {
            return -2;
        }
        if (_dimQ0 == 1) {
            int ij = 0;
            for (int j = 0; j < _n; j++) {
                ij += j;
                _qArray[ij++] += _q0[0];
            }
            if (_dimX0 != 0) {
                for (int i = 0; i < _n; i++) {
                    _x[i] += _q0[0] * _x0[i];
                }
            }
        } else if (_dimQ0 == _n) {
            int ij = 0;
            for (int j = 0; j < _n; j++) {
                ij += j;
                _qArray[ij++] += _q0[j];
            }
            if (_dimX0 != 0) {
                for (int i = 0; i < _n; i++) {
                    _x[i] += _q0[i] * _x0[i];
                }
            }
        } else if (_dimQ0 == _n * (_n + 1) / 2) {
            final int ijmax = _n * (_n + 1) / 2;
            for (int ij = 0; ij < ijmax; ij++) {
                _qArray[ij] += _q0[ij];
            }
            if (_dimX0 != 0) {
                for (int i = 0; i < _n; i++) {
                    for (int j = 0; j < i; j++) {
                        _x[i] += _q0[j + _larray[i]] * _x0[j];
                    }
                    for (int j = i; j < _n; j++) {
                        _x[i] += _q0[i + _larray[j]] * _x0[j];
                    }
                }
            }
        } else if (_dimQ0 == _n * _n) {
            int ij = 0;
            for (int j = 0; j < _n; j++) {
                for (int i = 0; i < j; i++) {
                    _qArray[ij++] += (_q0[i + _k[j]] + _q0[j + _k[i]]) / 2;
                }
                _qArray[ij++] += _q0[j + _k[j]];
            }
            if (_dimX0 != 0) {
                for (int i = 0; i < _n; i++) {
                    for (int j = 0; j < _n; j++) {
                        _x[i] += _q0[i + _k[j]] * _x0[j];
                    }
                }
            }
        } else if (_dimQ0 != 0) {
            return -2;
        }
        for (int ij = 0; ij < _n * (_n + 1) / 2; ij++) {
            _l[ij] = _qArray[ij];
        }
        if (Cholesky.calc(_l, _larray, _n) != 0) {
            return -1;
        }
        if (_q > 0) {
            for (int j = 0; j < _n; j++) {
                for (int i = 0; i < j; i++) {
                    _x[j] -= _l[i + _larray[j]] * _x[i];
                }
                _x[j] /= _l[j + _larray[j]];
            }
            for (int k = 0; k < _q; k++) {
                final int j0 = _k[k];
                for (int j = 0; j < _n; j++) {
                    for (int i = 0; i < j; i++) {
                        _c[j0 + j] -= _l[i + _larray[j]] * _c[j0 + i];
                    }
                    _c[j0 + j] /= _l[j + _larray[j]];
                }
            }
            for (int k = 0; k < _q; k++) {
                for (int i = 0; i < _n; i++) {
                    _d[k] -= _c[i + _k[k]] * _x[i];
                }
            }
            int ij = 0;
            for (int j = 0; j < _q; j++) {
                for (int i = 0; i <= j; i++) {
                    _m[ij] = 0;
                    for (int k = 0; k < _n; k++) {
                        _m[ij] += _c[k + _k[i]] * _c[k + _k[j]];
                    }
                    ij++;
                }
            }
            if (Cholesky.calc(_m, _larray, _q) != 0) {
                return -3;
            }
            Cholesky.solve(_m, _d, _larray, _q);
            for (int i = 0; i < _n; i++) {
                for (int k = 0; k < _q; k++) {
                    _x[i] += _c[i + _k[k]] * _d[k];
                }
            }
            for (int j = _n - 1; j >= 0; j--) {
                for (int i = j + 1; i < _n; i++) {
                    _x[j] -= _l[j + _larray[i]] * _x[i];
                }
                _x[j] /= _l[j + _larray[j]];
            }
        } else {
            Cholesky.solve(_l, _x, _larray, _n);
        }
        return 0;
    }
}
