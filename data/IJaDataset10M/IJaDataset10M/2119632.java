package org.deft.export.xslt;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.deft.exporter.AbstractExporter;
import org.deft.exporter.ExportException;
import org.deft.exporter.ExportRepositoryFacade;
import org.deft.exporter.SelectCssWizardPage;
import org.deft.exporter.XmlToXhtmlConverter;
import org.deft.helper.DeftLogger;
import org.deft.helper.OutputHelper;
import org.deft.repository.fragment.Chapter;
import org.deft.repository.fragment.CodeFile;
import org.deft.repository.fragment.CodeSnippet;
import org.deft.repository.fragment.Fragment;
import org.deft.repository.xfsr.reference.CodeSnippetRef;
import org.deft.share.helper.DocumentHelper;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XsltExporter extends AbstractExporter {

    private IPath absoluteXsltStylesheetFilename = null;

    private IPath absoluteDestinationFileName = null;

    private IPath absoluteDestinationBasePath = null;

    private ExportRepositoryFacade repository = null;

    private Document inputDocument = null;

    private List<CodeSnippetRef> codeReferences = null;

    private Chapter singleChapter = null;

    private boolean singleChapterExport = false;

    @Override
    protected void export(Fragment fragment, Map<String, Object> options, IProgressMonitor monitor) throws ExportException {
        absoluteXsltStylesheetFilename = new Path((String) options.get(XsltExporterConstants.XSLT_STYLESHEET_FILENAME));
        absoluteDestinationBasePath = new Path((String) options.get(XsltExporterConstants.DESTINATION_FOLDER));
        absoluteDestinationFileName = absoluteDestinationBasePath.append(XsltExporterConstants.DESTINATION_FILENAME);
        repository = new ExportRepositoryFacade();
        try {
            inputDocument = DocumentHelper.getDocumentBuilder().newDocument();
            inputDocument.setXmlStandalone(true);
        } catch (ParserConfigurationException e) {
            DeftLogger.log(Status.ERROR, "Parser could not be initialized during export.", e, XsltExporter.class);
            throw new ExportException(e.toString(), e);
        }
        codeReferences = new ArrayList<CodeSnippetRef>();
        try {
            Element tutor = inputDocument.createElement("tutorial");
            tutor.setAttribute("name", fragment.getName());
            inputDocument.appendChild(tutor);
            if (fragment instanceof Chapter) {
                singleChapterExport = true;
                importChapter((Chapter) fragment);
            } else {
                for (Fragment c : fragment.getChildren()) {
                    importChapter((Chapter) c);
                }
            }
            importCodeFiles();
        } catch (SAXException e) {
            throw new ExportException(e.getMessage(), e);
        } catch (IOException e) {
            throw new ExportException("Could not read chapter from disk.", e);
        }
        createTutorialEnvironment(absoluteDestinationBasePath, options);
        try {
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer(new StreamSource(absoluteXsltStylesheetFilename.toFile()));
            transformer.setParameter("outputdir", absoluteDestinationFileName.removeLastSegments(1).addTrailingSeparator());
            transformer.setParameter("filename", absoluteDestinationFileName.lastSegment());
            transformer.setParameter("singleChapter", Boolean.toString(singleChapterExport));
            transformer.setParameter("langXml", options.get(XsltExporterConstants.LANGUAGE_FILENAME));
            transformer.transform(new DOMSource(inputDocument), new StreamResult(absoluteDestinationFileName.toFile()));
        } catch (TransformerConfigurationException ex) {
            throw new ExportException(ex.toString(), ex);
        } catch (TransformerException ex) {
            throw new ExportException(ex.toString(), ex);
        }
    }

    private void importChapter(Chapter chapterFragment) throws SAXException, IOException {
        Chapter origChapter = chapterFragment;
        if (repository.isTutorialChapter(chapterFragment)) {
            origChapter = repository.getParentChapter(chapterFragment);
        }
        codeReferences.addAll(repository.getCodeSnippetReferences(origChapter));
        Element chapter = inputDocument.createElement("chapter");
        chapter.setAttribute("name", origChapter.getName());
        chapter.setAttribute("id", origChapter.getUUID().toString());
        Document chapterContent = repository.getChapterAsDocument(chapterFragment);
        chapter.appendChild(inputDocument.importNode(chapterContent.getDocumentElement(), true));
        NodeList coderefs = chapter.getElementsByTagName("coderef");
        for (int i = 0; i < coderefs.getLength(); ++i) {
            Element ref = (Element) coderefs.item(i);
            String codeSnippetId = ref.getAttribute("id");
            CodeSnippet codeSnippet = (CodeSnippet) repository.getFragment(UUID.fromString(codeSnippetId));
            String codeFileUUID = repository.getCodeSnippetReferences(codeSnippet).get(0).getCodeFileId().toString();
            ref.setAttribute("codefileUuid", codeFileUUID);
        }
        inputDocument.getDocumentElement().appendChild(chapter);
    }

    private void importCodeFiles() {
        List<CodeFile> codeFiles = new ArrayList<CodeFile>();
        for (CodeSnippetRef ref : codeReferences) {
            CodeFile codeFile = ref.getCodeFile();
            if (!codeFiles.contains(codeFile)) {
                codeFiles.add(codeFile);
            }
        }
        for (CodeFile codeFile : codeFiles) {
            Collection<CodeSnippetRef> codeSnippetRefs = repository.getCodeSnippetReferences(codeFile);
            if (singleChapter != null) {
                Iterator<CodeSnippetRef> it = codeSnippetRefs.iterator();
                while (it.hasNext()) {
                    CodeSnippetRef ref = it.next();
                    if (!ref.getChapter().equals(singleChapter)) {
                        it.remove();
                    }
                }
            }
            Document document = repository.getCodeFileAsDocument(codeFile, codeSnippetRefs);
            XmlToXhtmlConverter converter = new XmlToXhtmlConverter(true, true);
            Element sourcecode = (Element) document.getElementsByTagName("sourcecode").item(0);
            Element result = converter.convertCodeFile(sourcecode, document);
            result = (Element) inputDocument.adoptNode(result);
            Element body = (Element) inputDocument.getDocumentElement();
            Element codeFileElement = inputDocument.createElement("codefile");
            codeFileElement.setAttribute("id", codeFile.getUUID().toString());
            codeFileElement.appendChild(result);
            body.appendChild(codeFileElement);
        }
    }

    private void createTutorialEnvironment(IPath absoluteBasePath, Map<String, Object> options) throws ExportException {
        try {
            if (!absoluteBasePath.toFile().isDirectory() && (Boolean) options.get(XsltExporterConstants.CREATE_DIRECTORIES)) {
                System.err.println(absoluteDestinationBasePath.toString());
                if (absoluteDestinationBasePath.toFile().mkdirs() == false) {
                    throw new Exception("export directory could not be created");
                }
            } else if (!absoluteBasePath.toFile().isDirectory()) {
                throw new Exception("export directory does not exist");
            }
        } catch (Exception e) {
            throw new ExportException("Could not write to export directory", e);
        }
        absoluteBasePath = absoluteBasePath.append("files");
        String[] directories = { "chapters", "codefiles", "misc", "images" };
        for (String dir : directories) {
            absoluteBasePath.append(dir).toFile().mkdirs();
        }
        try {
            String cssFilename = (String) options.get(SelectCssWizardPage.CSS_FILENAME);
            OutputHelper.copyFile(new Path(cssFilename), absoluteBasePath.append("misc/style.css"));
            InputStream is = this.getClass().getResourceAsStream("/icons/link-left.png");
            OutputHelper.saveFile(is, absoluteBasePath.append("misc/link-left.png"));
            is = this.getClass().getResourceAsStream("/icons/link-right.png");
            OutputHelper.saveFile(is, absoluteBasePath.append("misc/link-right.png"));
            is = this.getClass().getResourceAsStream("/icons/info.png");
            OutputHelper.saveFile(is, absoluteBasePath.append("misc/info.png"));
        } catch (IOException e) {
            throw new ExportException("could not copy a file", e);
        }
    }
}
