package com.avdheshyadav.p4j.sample.server.dao.h2;

import com.avdheshyadav.p4j.jdbc.service.DataProvider;
import com.avdheshyadav.p4j.sample.server.dao.BookRepository;
import com.avdheshyadav.p4j.sample.server.dao.LibraryDataRepository;
import com.avdheshyadav.p4j.sample.server.dao.StudentRepository;

/**
 * 
 * @author  Avdhesh Yadav
 *
 */
public class H2SampleDataRepositoryImpl extends LibraryDataRepository {

    /**
	 * 
	 * @param daoService DAOService
	 */
    public H2SampleDataRepositoryImpl(DataProvider dataProvider) throws Exception {
        super(dataProvider);
    }

    /**
	 * 
	 */
    public StudentRepository getStudentRepository() {
        return new H2StudentRepoImpl(dataProvider.getDataFetcher());
    }

    /**
	 * 
	 */
    public BookRepository getBookRepository() {
        return new H2BookRepoImpl(dataProvider.getDataFetcher());
    }
}
