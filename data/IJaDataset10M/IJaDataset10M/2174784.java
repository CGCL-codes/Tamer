package com.chungco.rest.boxnet.service;

import java.util.List;
import com.chungco.core.Stack;
import com.chungco.core.xml.IXmlSaxParser;
import com.chungco.core.xml.XmlAttr;
import com.chungco.core.xml.XmlElement;
import com.chungco.rest.RestUtils;
import com.chungco.rest.boxnet.model.BoxFile;
import com.chungco.rest.boxnet.model.BoxFolder;
import com.chungco.rest.exception.MalformedXmlException;

public class FileListingService extends AbstractBoxService<FileListingResult> {

    protected static final String KEY_FOLDER_ID = "folder_id_here";

    protected static final String KEY_ONE_LEVEL = "one_level";

    protected static final String RESOURCE = "FileListingService.xml";

    @Override
    public String getEndpointURL() {
        final String sid = getParam(KEY_SID);
        return getBoxConfig().getBoxNetHost() + "/ping/" + sid;
    }

    @Override
    protected String loadXml() {
        return RestUtils.getXmlFromResource(this, RESOURCE);
    }

    @Override
    protected FileListingResult doParseXml(final String pXmlStr) throws MalformedXmlException {
        final FileListingResult result = new FileListingResult();
        final IXmlSaxParser sax2 = new IXmlSaxParser() {

            Stack<BoxFolder> folderStk;

            public void startDocument() {
                folderStk = new Stack<BoxFolder>();
            }

            public void startElement(final XmlElement pXml, final Stack<XmlElement> pParents) {
                final String xmlKey = pXml.key();
                if (BoxFolder.XML_FOLDER.equals(xmlKey)) {
                    BoxFolder newFolder = null;
                    if (folderStk.size() == 0) {
                        newFolder = new BoxFolder(null);
                        result.setFolder(newFolder);
                    } else {
                        BoxFolder parent = folderStk.peek();
                        newFolder = new BoxFolder(parent);
                    }
                    final List<XmlAttr> attrList = pXml.value();
                    for (final XmlAttr valuePair : attrList) {
                        if (BoxFolder.XML_FOLDER_ID.equals(valuePair.key())) {
                            newFolder.setId(valuePair.value());
                        } else if (BoxFolder.XML_FOLDER_NAME.equals(valuePair.key())) {
                            newFolder.setName(valuePair.value());
                        } else if (BoxFolder.XML_FOLDER_SHARED.equals(valuePair.key())) {
                            newFolder.setShared(valuePair.value());
                        }
                    }
                    folderStk.push(newFolder);
                } else if (BoxFolder.XML_FOLDERS.equals(pXml.key())) {
                } else if (BoxFile.XML_FILES.equals(pXml.key())) {
                } else if (BoxFile.XML_FILE.equals(xmlKey)) {
                    final BoxFolder parent = folderStk.peek();
                    final List<XmlAttr> attrList = pXml.value();
                    final BoxFile file = new BoxFile();
                    parent.addFile(file);
                    file.setFolderId(parent.getId());
                    for (XmlAttr valuePair : attrList) {
                        if (BoxFile.XML_FILE_ID.equals(valuePair.key())) {
                            file.setId(valuePair.value());
                        } else if (BoxFile.XML_FILE_NAME.equals(valuePair.key())) {
                            file.setName(valuePair.value());
                        } else if (BoxFile.XML_FILE_KEYWORD.equals(valuePair.key())) {
                            file.setKeywords(valuePair.value());
                        } else if (BoxFile.XML_FILE_SHARED.equals(valuePair.key())) {
                            file.setShared(valuePair.value());
                        } else if (BoxFile.XML_FILE_SIZE.equals(valuePair.key())) {
                            file.setSize(Long.parseLong(valuePair.value()));
                        } else if (BoxFile.XML_FILE_CREATED.equals(valuePair.key())) {
                            file.setDateCreated(Long.parseLong(valuePair.value()));
                        } else if (BoxFile.XML_FILE_UPDATED.equals(valuePair.key())) {
                            file.setDateUpdated(Long.parseLong(valuePair.value()));
                        } else if (BoxFile.XML_FILE_THUMBNAIL.equals(valuePair.key())) {
                            file.setThumbnail(getBoxConfig().getBoxNetHost() + "/" + valuePair.value());
                        }
                    }
                } else if (BoxFile.XML_TAGS.equals(xmlKey)) {
                }
            }

            public void endElement(final XmlElement pXml, final Stack<XmlElement> pParents) {
                final String key = pXml.key();
                if (BoxFolder.XML_FOLDER.equals(key)) {
                    folderStk.pop();
                }
            }

            public void startContent(final XmlElement pXml, final String pText, final Stack<XmlElement> pParents) {
                if (pXml.key().equals(AuthorizationResult.XML_STATUS)) {
                    result.setStatus(pText);
                }
            }
        };
        try {
            RestUtils.parseXml2(pXmlStr, sax2);
        } catch (MalformedXmlException mxe) {
            throw new MalformedXmlException("Couldn't get file listings.", mxe.getCause());
        }
        return result;
    }

    public void setFolderId(final String pId) {
        setParam(KEY_FOLDER_ID, pId);
    }

    public void setLevel(final String pLevel) {
        Integer value;
        if (pLevel == null) {
            value = 0;
        } else {
            value = Integer.parseInt(pLevel);
            if (value < 0) {
                throw new IllegalArgumentException("one_level must be greater than 0");
            }
        }
        setParam(KEY_ONE_LEVEL, value.toString());
    }
}
