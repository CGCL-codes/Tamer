package br.ufmg.saotome.arangi.commons.report;

import java.util.Collection;

/**
 * 
 * 
 * @author Marcelo Gomes
 *
 */
public class JRArrayCollectionDataSource extends JRArrayIteratorDataSource {

    public JRArrayCollectionDataSource(Collection list, String[] fields) {
        super(list.iterator(), fields);
    }
}
