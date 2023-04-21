package org.az.hhp.tools;

public class Sigmoids {

    public static double cosinusoidal(final double x) {
        return (1f - Math.cos(x * Math.PI)) / 2f;
    }

    /**
     * 
     * 1+\frac{2*(\sigma-1)}{1+e^(\frac{-2*(x-\sigma)}{\sigma-1})}
     * 
     * 
     * 
     * 
     * 
     * 
     \sigma_x(x) =   \begin{cases}
    & 1+\frac{2(\sigma_0-1)}{1+e^{^{ \frac{-2(x-\sigma_0)}{\sigma_0-1} }}} \text{ if } x<\sigma_0 \\ 
    & 
    2 \sigma_0-max+ 
    \frac {
       2(max-\sigma_0)   
    }{1+e^{\frac {-2*(x-\sigma_0)}{max-\sigma_0}}}

    \text{ if } x\geqslant \sigma_0 
    \end{cases}


     * 
     *   2(s-1)/(1 + exp (-2(x-s)/(s-1)))  where s<2  
     *    2s-5+2(5-s)/(1+ exp(-2(x-s)/(5-s))) where s>2  
     * 
     * @author zaborsky
     *
     */
    public static double compound(final double x, final double inflectionPoint) {
        if (x < inflectionPoint) {
            return x;
        } else {
            return scaledTo15(x, inflectionPoint);
        }
    }

    public static double exponential(final double x) {
        return 1d / (1d + Math.exp(-x));
    }

    public static double exponential(final double x, final float bias) {
        return 1d / (1d + Math.exp(-x * bias));
    }

    private static double scaledTo15(final double x, final double inflectionPoint) {
        return 2 * inflectionPoint - 15 + 2 * (15 - inflectionPoint) / (1 + Math.exp(-2 * (x - inflectionPoint) / (15 - inflectionPoint)));
    }
}
