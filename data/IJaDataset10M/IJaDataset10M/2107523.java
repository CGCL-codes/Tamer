package com.dcivision.framework;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MultiSearcher;
import org.apache.lucene.search.ParallelMultiSearcher;
import org.apache.lucene.search.RemoteSearchable;
import org.apache.lucene.search.Searchable;
import org.apache.lucene.search.Searcher;

public class SearchServer {

    public static final String REVISION = "$Revision: 1.3 $";

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Usage: SearchServer <basedir> <jndi_name>");
            System.exit(-1);
        }
        String basedir = args[0];
        String baseName = args[1];
        Searchable[] searchables = new Searchable[1];
        searchables[0] = new IndexSearcher(basedir);
        LocateRegistry.createRegistry(1099);
        Searcher multiSearcher = new MultiSearcher(searchables);
        RemoteSearchable multiImpl = new RemoteSearchable(multiSearcher);
        Naming.rebind("//localhost/ParaDM_Multi_" + baseName, multiImpl);
        Searcher parallelSearcher = new ParallelMultiSearcher(searchables);
        RemoteSearchable parallelImpl = new RemoteSearchable(parallelSearcher);
        Naming.rebind("//localhost/ParaDM_Parallel_" + baseName, parallelImpl);
        System.out.println("Server started");
    }
}
