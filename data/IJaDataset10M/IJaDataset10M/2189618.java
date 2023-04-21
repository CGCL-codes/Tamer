package de.toolforge.googlechartwrapper;

import de.toolforge.googlechartwrapper.coder.IEncoder;

public interface IChart {

    public abstract IEncoder getEncoder();

    /**
	 * Returns the generated chart url with a default Chart API's location.
	 * Google Chart API URLs must be in the following format: 
	 * &lt;chart api location&gt;&lt;parameter 1&gt;&amp;&lt;parameter 2&gt;
	 * &amp;&lt;parameter n&gt; <br>
	 * 
	 * For example: http://chart.apis.google.com/chart?&lt;parameter 1&gt;
	 * &amp;&lt;parameter 2&gt;
	 * &amp;&lt;parameter n&gt;
	 * 
	 * For other url specific details refer to the google API.
	 */
    public abstract String getUrl();
}
