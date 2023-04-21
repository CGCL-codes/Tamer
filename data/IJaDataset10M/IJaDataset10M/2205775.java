package org.escapek.server.cim;

import org.escapek.domain.MOFCompilationReport;

public interface IMOFContentProvider {

    public void include(String fileName, MOFCompilationReport report);
}
