package com.jPianoBar;

import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: vincent
 * Date: 7/21/11
 * Time: 8:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class PandoraSearcher_UT {

    @Test
    public void testSearch() throws Exception {
        PandoraAccount account = new PandoraLogin().login("vincentjames501@gmail.com", "gateway63043");
        PandoraSearchResults searchResults = new PandoraSearcher().search("lady gaga", account);
        System.out.println();
    }
}
