package org.capelin.transaction.utils;

/**
 * 
 * <a href="http://code.google.com/p/capline-opac/">Capelin-opac</a>
 * License: GNU AGPL v3 | http://www.gnu.org/licenses/agpl.html
 * 
 * Interface
 * 
 * 
 * @author Jing Xiao <jing.xiao.ca at gmail dot com>
 * 
 * 
 */
public interface TXRecrodImporter {

    public static final int IMPORT_ONLY = 1;

    public static final int INDEXES_THEN_IMPORT = 2;

    public static final int INDEX_ONLY = 3;

    public void run(int command) throws Exception;
}
