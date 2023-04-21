package com.aptana.ide.editor.sql;

import com.aptana.ide.editor.sql.parsing.SQLMimeType;
import com.aptana.ide.editors.unified.BaseFileLanguageService;
import com.aptana.ide.editors.unified.FileService;
import com.aptana.ide.editors.unified.IFileServiceFactory;
import com.aptana.ide.editors.unified.IFileSourceProvider;
import com.aptana.ide.editors.unified.ParentOffsetMapper;
import com.aptana.ide.editors.unified.errors.UnifiedErrorManager;
import com.aptana.ide.parsing.IParseState;
import com.aptana.ide.parsing.IParser;

/**
 * @author Kevin Lindsey
 *
 */
public final class SQLFileServiceFactory implements IFileServiceFactory {

    private static SQLFileServiceFactory instance;

    /**
	 * SQLFileServiceFactory
	 */
    private SQLFileServiceFactory() {
    }

    /**
	 * @see com.aptana.ide.editors.unified.IFileServiceFactory#createFileService(com.aptana.ide.editors.unified.IFileSourceProvider)
	 */
    public FileService createFileService(IFileSourceProvider sourceProvider) {
        IParser parser = null;
        IParseState parseState = null;
        FileService fileService = new FileService(parser, parseState, sourceProvider, SQLMimeType.MimeType);
        ParentOffsetMapper parentMapper = new ParentOffsetMapper(fileService);
        BaseFileLanguageService languageService = new BaseFileLanguageService(fileService, parseState, parser, parentMapper);
        fileService.setErrorManager(new UnifiedErrorManager(fileService, SQLMimeType.MimeType));
        fileService.addLanguageService(SQLMimeType.MimeType, languageService);
        return fileService;
    }

    /**
	 * getInstance
	 *
	 * @return IFileServiceFactory
	 */
    public static IFileServiceFactory getInstance() {
        if (instance == null) {
            instance = new SQLFileServiceFactory();
        }
        return instance;
    }
}
