package com.mobilvox.ossi.mojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import com.mobilvox.ossi.mojo.js.compression.CompressionManager;

/**
 * <code>JsSiteCompressionMojo</code>
 * <p>
 * Compress all JavaScript within the build-output-directory Maven site
 * directory.
 * 
 * @author Adam Altemus
 * 
 * @goal compress-site
 */
public class JsSiteCompressionMojo extends AbstractJsCompressMojo {

    /**
     * Execute method of compress MOJO.
     * 
     * @throws MojoExecutionException
     * @throws MojoFailureException
     * @see org.apache.maven.plugin.AbstractMojo#execute()
     */
    public void execute() throws MojoExecutionException, MojoFailureException {
        CompressionManager cm = new CompressionManager();
        cm.runSiteCompression();
    }
}
