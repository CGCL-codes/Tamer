package org.openmeetings.app.persistence.beans.flvrecord.xsd;

public class FlvRecording implements java.io.Serializable {

    private java.lang.String alternateDownload;

    private java.lang.String comment;

    private org.openmeetings.app.persistence.beans.user.xsd.Users creator;

    private java.lang.String deleted;

    private java.lang.String fileHash;

    private java.lang.String fileName;

    private java.lang.Long fileSize;

    private java.lang.Integer flvHeight;

    private java.lang.Long flvRecordingId;

    private java.lang.Object flvRecordingLog;

    private java.lang.Object flvRecordingMetaData;

    private java.lang.Integer flvWidth;

    private java.lang.Integer height;

    private java.util.Date inserted;

    private java.lang.Long insertedBy;

    private java.lang.Boolean isFolder;

    private java.lang.Boolean isImage;

    private java.lang.Boolean isInterview;

    private java.lang.Boolean isPresentation;

    private java.lang.Boolean isRecording;

    private java.lang.Long organization_id;

    private java.lang.Long ownerId;

    private java.lang.Long parentFileExplorerItemId;

    private java.lang.String previewImage;

    private java.lang.Integer progressPostProcessing;

    private java.util.Date recordEnd;

    private java.util.Date recordStart;

    private java.lang.String recorderStreamId;

    private org.openmeetings.app.persistence.beans.rooms.xsd.Rooms room;

    private java.lang.Long room_id;

    private java.util.Date updated;

    private java.lang.Integer width;

    public FlvRecording() {
    }

    public FlvRecording(java.lang.String alternateDownload, java.lang.String comment, org.openmeetings.app.persistence.beans.user.xsd.Users creator, java.lang.String deleted, java.lang.String fileHash, java.lang.String fileName, java.lang.Long fileSize, java.lang.Integer flvHeight, java.lang.Long flvRecordingId, java.lang.Object flvRecordingLog, java.lang.Object flvRecordingMetaData, java.lang.Integer flvWidth, java.lang.Integer height, java.util.Date inserted, java.lang.Long insertedBy, java.lang.Boolean isFolder, java.lang.Boolean isImage, java.lang.Boolean isInterview, java.lang.Boolean isPresentation, java.lang.Boolean isRecording, java.lang.Long organization_id, java.lang.Long ownerId, java.lang.Long parentFileExplorerItemId, java.lang.String previewImage, java.lang.Integer progressPostProcessing, java.util.Date recordEnd, java.util.Date recordStart, java.lang.String recorderStreamId, org.openmeetings.app.persistence.beans.rooms.xsd.Rooms room, java.lang.Long room_id, java.util.Date updated, java.lang.Integer width) {
        this.alternateDownload = alternateDownload;
        this.comment = comment;
        this.creator = creator;
        this.deleted = deleted;
        this.fileHash = fileHash;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.flvHeight = flvHeight;
        this.flvRecordingId = flvRecordingId;
        this.flvRecordingLog = flvRecordingLog;
        this.flvRecordingMetaData = flvRecordingMetaData;
        this.flvWidth = flvWidth;
        this.height = height;
        this.inserted = inserted;
        this.insertedBy = insertedBy;
        this.isFolder = isFolder;
        this.isImage = isImage;
        this.isInterview = isInterview;
        this.isPresentation = isPresentation;
        this.isRecording = isRecording;
        this.organization_id = organization_id;
        this.ownerId = ownerId;
        this.parentFileExplorerItemId = parentFileExplorerItemId;
        this.previewImage = previewImage;
        this.progressPostProcessing = progressPostProcessing;
        this.recordEnd = recordEnd;
        this.recordStart = recordStart;
        this.recorderStreamId = recorderStreamId;
        this.room = room;
        this.room_id = room_id;
        this.updated = updated;
        this.width = width;
    }

    /**
     * Gets the alternateDownload value for this FlvRecording.
     * 
     * @return alternateDownload
     */
    public java.lang.String getAlternateDownload() {
        return alternateDownload;
    }

    /**
     * Sets the alternateDownload value for this FlvRecording.
     * 
     * @param alternateDownload
     */
    public void setAlternateDownload(java.lang.String alternateDownload) {
        this.alternateDownload = alternateDownload;
    }

    /**
     * Gets the comment value for this FlvRecording.
     * 
     * @return comment
     */
    public java.lang.String getComment() {
        return comment;
    }

    /**
     * Sets the comment value for this FlvRecording.
     * 
     * @param comment
     */
    public void setComment(java.lang.String comment) {
        this.comment = comment;
    }

    /**
     * Gets the creator value for this FlvRecording.
     * 
     * @return creator
     */
    public org.openmeetings.app.persistence.beans.user.xsd.Users getCreator() {
        return creator;
    }

    /**
     * Sets the creator value for this FlvRecording.
     * 
     * @param creator
     */
    public void setCreator(org.openmeetings.app.persistence.beans.user.xsd.Users creator) {
        this.creator = creator;
    }

    /**
     * Gets the deleted value for this FlvRecording.
     * 
     * @return deleted
     */
    public java.lang.String getDeleted() {
        return deleted;
    }

    /**
     * Sets the deleted value for this FlvRecording.
     * 
     * @param deleted
     */
    public void setDeleted(java.lang.String deleted) {
        this.deleted = deleted;
    }

    /**
     * Gets the fileHash value for this FlvRecording.
     * 
     * @return fileHash
     */
    public java.lang.String getFileHash() {
        return fileHash;
    }

    /**
     * Sets the fileHash value for this FlvRecording.
     * 
     * @param fileHash
     */
    public void setFileHash(java.lang.String fileHash) {
        this.fileHash = fileHash;
    }

    /**
     * Gets the fileName value for this FlvRecording.
     * 
     * @return fileName
     */
    public java.lang.String getFileName() {
        return fileName;
    }

    /**
     * Sets the fileName value for this FlvRecording.
     * 
     * @param fileName
     */
    public void setFileName(java.lang.String fileName) {
        this.fileName = fileName;
    }

    /**
     * Gets the fileSize value for this FlvRecording.
     * 
     * @return fileSize
     */
    public java.lang.Long getFileSize() {
        return fileSize;
    }

    /**
     * Sets the fileSize value for this FlvRecording.
     * 
     * @param fileSize
     */
    public void setFileSize(java.lang.Long fileSize) {
        this.fileSize = fileSize;
    }

    /**
     * Gets the flvHeight value for this FlvRecording.
     * 
     * @return flvHeight
     */
    public java.lang.Integer getFlvHeight() {
        return flvHeight;
    }

    /**
     * Sets the flvHeight value for this FlvRecording.
     * 
     * @param flvHeight
     */
    public void setFlvHeight(java.lang.Integer flvHeight) {
        this.flvHeight = flvHeight;
    }

    /**
     * Gets the flvRecordingId value for this FlvRecording.
     * 
     * @return flvRecordingId
     */
    public java.lang.Long getFlvRecordingId() {
        return flvRecordingId;
    }

    /**
     * Sets the flvRecordingId value for this FlvRecording.
     * 
     * @param flvRecordingId
     */
    public void setFlvRecordingId(java.lang.Long flvRecordingId) {
        this.flvRecordingId = flvRecordingId;
    }

    /**
     * Gets the flvRecordingLog value for this FlvRecording.
     * 
     * @return flvRecordingLog
     */
    public java.lang.Object getFlvRecordingLog() {
        return flvRecordingLog;
    }

    /**
     * Sets the flvRecordingLog value for this FlvRecording.
     * 
     * @param flvRecordingLog
     */
    public void setFlvRecordingLog(java.lang.Object flvRecordingLog) {
        this.flvRecordingLog = flvRecordingLog;
    }

    /**
     * Gets the flvRecordingMetaData value for this FlvRecording.
     * 
     * @return flvRecordingMetaData
     */
    public java.lang.Object getFlvRecordingMetaData() {
        return flvRecordingMetaData;
    }

    /**
     * Sets the flvRecordingMetaData value for this FlvRecording.
     * 
     * @param flvRecordingMetaData
     */
    public void setFlvRecordingMetaData(java.lang.Object flvRecordingMetaData) {
        this.flvRecordingMetaData = flvRecordingMetaData;
    }

    /**
     * Gets the flvWidth value for this FlvRecording.
     * 
     * @return flvWidth
     */
    public java.lang.Integer getFlvWidth() {
        return flvWidth;
    }

    /**
     * Sets the flvWidth value for this FlvRecording.
     * 
     * @param flvWidth
     */
    public void setFlvWidth(java.lang.Integer flvWidth) {
        this.flvWidth = flvWidth;
    }

    /**
     * Gets the height value for this FlvRecording.
     * 
     * @return height
     */
    public java.lang.Integer getHeight() {
        return height;
    }

    /**
     * Sets the height value for this FlvRecording.
     * 
     * @param height
     */
    public void setHeight(java.lang.Integer height) {
        this.height = height;
    }

    /**
     * Gets the inserted value for this FlvRecording.
     * 
     * @return inserted
     */
    public java.util.Date getInserted() {
        return inserted;
    }

    /**
     * Sets the inserted value for this FlvRecording.
     * 
     * @param inserted
     */
    public void setInserted(java.util.Date inserted) {
        this.inserted = inserted;
    }

    /**
     * Gets the insertedBy value for this FlvRecording.
     * 
     * @return insertedBy
     */
    public java.lang.Long getInsertedBy() {
        return insertedBy;
    }

    /**
     * Sets the insertedBy value for this FlvRecording.
     * 
     * @param insertedBy
     */
    public void setInsertedBy(java.lang.Long insertedBy) {
        this.insertedBy = insertedBy;
    }

    /**
     * Gets the isFolder value for this FlvRecording.
     * 
     * @return isFolder
     */
    public java.lang.Boolean getIsFolder() {
        return isFolder;
    }

    /**
     * Sets the isFolder value for this FlvRecording.
     * 
     * @param isFolder
     */
    public void setIsFolder(java.lang.Boolean isFolder) {
        this.isFolder = isFolder;
    }

    /**
     * Gets the isImage value for this FlvRecording.
     * 
     * @return isImage
     */
    public java.lang.Boolean getIsImage() {
        return isImage;
    }

    /**
     * Sets the isImage value for this FlvRecording.
     * 
     * @param isImage
     */
    public void setIsImage(java.lang.Boolean isImage) {
        this.isImage = isImage;
    }

    /**
     * Gets the isInterview value for this FlvRecording.
     * 
     * @return isInterview
     */
    public java.lang.Boolean getIsInterview() {
        return isInterview;
    }

    /**
     * Sets the isInterview value for this FlvRecording.
     * 
     * @param isInterview
     */
    public void setIsInterview(java.lang.Boolean isInterview) {
        this.isInterview = isInterview;
    }

    /**
     * Gets the isPresentation value for this FlvRecording.
     * 
     * @return isPresentation
     */
    public java.lang.Boolean getIsPresentation() {
        return isPresentation;
    }

    /**
     * Sets the isPresentation value for this FlvRecording.
     * 
     * @param isPresentation
     */
    public void setIsPresentation(java.lang.Boolean isPresentation) {
        this.isPresentation = isPresentation;
    }

    /**
     * Gets the isRecording value for this FlvRecording.
     * 
     * @return isRecording
     */
    public java.lang.Boolean getIsRecording() {
        return isRecording;
    }

    /**
     * Sets the isRecording value for this FlvRecording.
     * 
     * @param isRecording
     */
    public void setIsRecording(java.lang.Boolean isRecording) {
        this.isRecording = isRecording;
    }

    /**
     * Gets the organization_id value for this FlvRecording.
     * 
     * @return organization_id
     */
    public java.lang.Long getOrganization_id() {
        return organization_id;
    }

    /**
     * Sets the organization_id value for this FlvRecording.
     * 
     * @param organization_id
     */
    public void setOrganization_id(java.lang.Long organization_id) {
        this.organization_id = organization_id;
    }

    /**
     * Gets the ownerId value for this FlvRecording.
     * 
     * @return ownerId
     */
    public java.lang.Long getOwnerId() {
        return ownerId;
    }

    /**
     * Sets the ownerId value for this FlvRecording.
     * 
     * @param ownerId
     */
    public void setOwnerId(java.lang.Long ownerId) {
        this.ownerId = ownerId;
    }

    /**
     * Gets the parentFileExplorerItemId value for this FlvRecording.
     * 
     * @return parentFileExplorerItemId
     */
    public java.lang.Long getParentFileExplorerItemId() {
        return parentFileExplorerItemId;
    }

    /**
     * Sets the parentFileExplorerItemId value for this FlvRecording.
     * 
     * @param parentFileExplorerItemId
     */
    public void setParentFileExplorerItemId(java.lang.Long parentFileExplorerItemId) {
        this.parentFileExplorerItemId = parentFileExplorerItemId;
    }

    /**
     * Gets the previewImage value for this FlvRecording.
     * 
     * @return previewImage
     */
    public java.lang.String getPreviewImage() {
        return previewImage;
    }

    /**
     * Sets the previewImage value for this FlvRecording.
     * 
     * @param previewImage
     */
    public void setPreviewImage(java.lang.String previewImage) {
        this.previewImage = previewImage;
    }

    /**
     * Gets the progressPostProcessing value for this FlvRecording.
     * 
     * @return progressPostProcessing
     */
    public java.lang.Integer getProgressPostProcessing() {
        return progressPostProcessing;
    }

    /**
     * Sets the progressPostProcessing value for this FlvRecording.
     * 
     * @param progressPostProcessing
     */
    public void setProgressPostProcessing(java.lang.Integer progressPostProcessing) {
        this.progressPostProcessing = progressPostProcessing;
    }

    /**
     * Gets the recordEnd value for this FlvRecording.
     * 
     * @return recordEnd
     */
    public java.util.Date getRecordEnd() {
        return recordEnd;
    }

    /**
     * Sets the recordEnd value for this FlvRecording.
     * 
     * @param recordEnd
     */
    public void setRecordEnd(java.util.Date recordEnd) {
        this.recordEnd = recordEnd;
    }

    /**
     * Gets the recordStart value for this FlvRecording.
     * 
     * @return recordStart
     */
    public java.util.Date getRecordStart() {
        return recordStart;
    }

    /**
     * Sets the recordStart value for this FlvRecording.
     * 
     * @param recordStart
     */
    public void setRecordStart(java.util.Date recordStart) {
        this.recordStart = recordStart;
    }

    /**
     * Gets the recorderStreamId value for this FlvRecording.
     * 
     * @return recorderStreamId
     */
    public java.lang.String getRecorderStreamId() {
        return recorderStreamId;
    }

    /**
     * Sets the recorderStreamId value for this FlvRecording.
     * 
     * @param recorderStreamId
     */
    public void setRecorderStreamId(java.lang.String recorderStreamId) {
        this.recorderStreamId = recorderStreamId;
    }

    /**
     * Gets the room value for this FlvRecording.
     * 
     * @return room
     */
    public org.openmeetings.app.persistence.beans.rooms.xsd.Rooms getRoom() {
        return room;
    }

    /**
     * Sets the room value for this FlvRecording.
     * 
     * @param room
     */
    public void setRoom(org.openmeetings.app.persistence.beans.rooms.xsd.Rooms room) {
        this.room = room;
    }

    /**
     * Gets the room_id value for this FlvRecording.
     * 
     * @return room_id
     */
    public java.lang.Long getRoom_id() {
        return room_id;
    }

    /**
     * Sets the room_id value for this FlvRecording.
     * 
     * @param room_id
     */
    public void setRoom_id(java.lang.Long room_id) {
        this.room_id = room_id;
    }

    /**
     * Gets the updated value for this FlvRecording.
     * 
     * @return updated
     */
    public java.util.Date getUpdated() {
        return updated;
    }

    /**
     * Sets the updated value for this FlvRecording.
     * 
     * @param updated
     */
    public void setUpdated(java.util.Date updated) {
        this.updated = updated;
    }

    /**
     * Gets the width value for this FlvRecording.
     * 
     * @return width
     */
    public java.lang.Integer getWidth() {
        return width;
    }

    /**
     * Sets the width value for this FlvRecording.
     * 
     * @param width
     */
    public void setWidth(java.lang.Integer width) {
        this.width = width;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof FlvRecording)) return false;
        FlvRecording other = (FlvRecording) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.alternateDownload == null && other.getAlternateDownload() == null) || (this.alternateDownload != null && this.alternateDownload.equals(other.getAlternateDownload()))) && ((this.comment == null && other.getComment() == null) || (this.comment != null && this.comment.equals(other.getComment()))) && ((this.creator == null && other.getCreator() == null) || (this.creator != null && this.creator.equals(other.getCreator()))) && ((this.deleted == null && other.getDeleted() == null) || (this.deleted != null && this.deleted.equals(other.getDeleted()))) && ((this.fileHash == null && other.getFileHash() == null) || (this.fileHash != null && this.fileHash.equals(other.getFileHash()))) && ((this.fileName == null && other.getFileName() == null) || (this.fileName != null && this.fileName.equals(other.getFileName()))) && ((this.fileSize == null && other.getFileSize() == null) || (this.fileSize != null && this.fileSize.equals(other.getFileSize()))) && ((this.flvHeight == null && other.getFlvHeight() == null) || (this.flvHeight != null && this.flvHeight.equals(other.getFlvHeight()))) && ((this.flvRecordingId == null && other.getFlvRecordingId() == null) || (this.flvRecordingId != null && this.flvRecordingId.equals(other.getFlvRecordingId()))) && ((this.flvRecordingLog == null && other.getFlvRecordingLog() == null) || (this.flvRecordingLog != null && this.flvRecordingLog.equals(other.getFlvRecordingLog()))) && ((this.flvRecordingMetaData == null && other.getFlvRecordingMetaData() == null) || (this.flvRecordingMetaData != null && this.flvRecordingMetaData.equals(other.getFlvRecordingMetaData()))) && ((this.flvWidth == null && other.getFlvWidth() == null) || (this.flvWidth != null && this.flvWidth.equals(other.getFlvWidth()))) && ((this.height == null && other.getHeight() == null) || (this.height != null && this.height.equals(other.getHeight()))) && ((this.inserted == null && other.getInserted() == null) || (this.inserted != null && this.inserted.equals(other.getInserted()))) && ((this.insertedBy == null && other.getInsertedBy() == null) || (this.insertedBy != null && this.insertedBy.equals(other.getInsertedBy()))) && ((this.isFolder == null && other.getIsFolder() == null) || (this.isFolder != null && this.isFolder.equals(other.getIsFolder()))) && ((this.isImage == null && other.getIsImage() == null) || (this.isImage != null && this.isImage.equals(other.getIsImage()))) && ((this.isInterview == null && other.getIsInterview() == null) || (this.isInterview != null && this.isInterview.equals(other.getIsInterview()))) && ((this.isPresentation == null && other.getIsPresentation() == null) || (this.isPresentation != null && this.isPresentation.equals(other.getIsPresentation()))) && ((this.isRecording == null && other.getIsRecording() == null) || (this.isRecording != null && this.isRecording.equals(other.getIsRecording()))) && ((this.organization_id == null && other.getOrganization_id() == null) || (this.organization_id != null && this.organization_id.equals(other.getOrganization_id()))) && ((this.ownerId == null && other.getOwnerId() == null) || (this.ownerId != null && this.ownerId.equals(other.getOwnerId()))) && ((this.parentFileExplorerItemId == null && other.getParentFileExplorerItemId() == null) || (this.parentFileExplorerItemId != null && this.parentFileExplorerItemId.equals(other.getParentFileExplorerItemId()))) && ((this.previewImage == null && other.getPreviewImage() == null) || (this.previewImage != null && this.previewImage.equals(other.getPreviewImage()))) && ((this.progressPostProcessing == null && other.getProgressPostProcessing() == null) || (this.progressPostProcessing != null && this.progressPostProcessing.equals(other.getProgressPostProcessing()))) && ((this.recordEnd == null && other.getRecordEnd() == null) || (this.recordEnd != null && this.recordEnd.equals(other.getRecordEnd()))) && ((this.recordStart == null && other.getRecordStart() == null) || (this.recordStart != null && this.recordStart.equals(other.getRecordStart()))) && ((this.recorderStreamId == null && other.getRecorderStreamId() == null) || (this.recorderStreamId != null && this.recorderStreamId.equals(other.getRecorderStreamId()))) && ((this.room == null && other.getRoom() == null) || (this.room != null && this.room.equals(other.getRoom()))) && ((this.room_id == null && other.getRoom_id() == null) || (this.room_id != null && this.room_id.equals(other.getRoom_id()))) && ((this.updated == null && other.getUpdated() == null) || (this.updated != null && this.updated.equals(other.getUpdated()))) && ((this.width == null && other.getWidth() == null) || (this.width != null && this.width.equals(other.getWidth())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;

    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getAlternateDownload() != null) {
            _hashCode += getAlternateDownload().hashCode();
        }
        if (getComment() != null) {
            _hashCode += getComment().hashCode();
        }
        if (getCreator() != null) {
            _hashCode += getCreator().hashCode();
        }
        if (getDeleted() != null) {
            _hashCode += getDeleted().hashCode();
        }
        if (getFileHash() != null) {
            _hashCode += getFileHash().hashCode();
        }
        if (getFileName() != null) {
            _hashCode += getFileName().hashCode();
        }
        if (getFileSize() != null) {
            _hashCode += getFileSize().hashCode();
        }
        if (getFlvHeight() != null) {
            _hashCode += getFlvHeight().hashCode();
        }
        if (getFlvRecordingId() != null) {
            _hashCode += getFlvRecordingId().hashCode();
        }
        if (getFlvRecordingLog() != null) {
            _hashCode += getFlvRecordingLog().hashCode();
        }
        if (getFlvRecordingMetaData() != null) {
            _hashCode += getFlvRecordingMetaData().hashCode();
        }
        if (getFlvWidth() != null) {
            _hashCode += getFlvWidth().hashCode();
        }
        if (getHeight() != null) {
            _hashCode += getHeight().hashCode();
        }
        if (getInserted() != null) {
            _hashCode += getInserted().hashCode();
        }
        if (getInsertedBy() != null) {
            _hashCode += getInsertedBy().hashCode();
        }
        if (getIsFolder() != null) {
            _hashCode += getIsFolder().hashCode();
        }
        if (getIsImage() != null) {
            _hashCode += getIsImage().hashCode();
        }
        if (getIsInterview() != null) {
            _hashCode += getIsInterview().hashCode();
        }
        if (getIsPresentation() != null) {
            _hashCode += getIsPresentation().hashCode();
        }
        if (getIsRecording() != null) {
            _hashCode += getIsRecording().hashCode();
        }
        if (getOrganization_id() != null) {
            _hashCode += getOrganization_id().hashCode();
        }
        if (getOwnerId() != null) {
            _hashCode += getOwnerId().hashCode();
        }
        if (getParentFileExplorerItemId() != null) {
            _hashCode += getParentFileExplorerItemId().hashCode();
        }
        if (getPreviewImage() != null) {
            _hashCode += getPreviewImage().hashCode();
        }
        if (getProgressPostProcessing() != null) {
            _hashCode += getProgressPostProcessing().hashCode();
        }
        if (getRecordEnd() != null) {
            _hashCode += getRecordEnd().hashCode();
        }
        if (getRecordStart() != null) {
            _hashCode += getRecordStart().hashCode();
        }
        if (getRecorderStreamId() != null) {
            _hashCode += getRecorderStreamId().hashCode();
        }
        if (getRoom() != null) {
            _hashCode += getRoom().hashCode();
        }
        if (getRoom_id() != null) {
            _hashCode += getRoom_id().hashCode();
        }
        if (getUpdated() != null) {
            _hashCode += getUpdated().hashCode();
        }
        if (getWidth() != null) {
            _hashCode += getWidth().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(FlvRecording.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://flvrecord.beans.persistence.app.openmeetings.org/xsd", "FlvRecording"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("alternateDownload");
        elemField.setXmlName(new javax.xml.namespace.QName("http://flvrecord.beans.persistence.app.openmeetings.org/xsd", "alternateDownload"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("comment");
        elemField.setXmlName(new javax.xml.namespace.QName("http://flvrecord.beans.persistence.app.openmeetings.org/xsd", "comment"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("creator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://flvrecord.beans.persistence.app.openmeetings.org/xsd", "creator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://user.beans.persistence.app.openmeetings.org/xsd", "Users"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("deleted");
        elemField.setXmlName(new javax.xml.namespace.QName("http://flvrecord.beans.persistence.app.openmeetings.org/xsd", "deleted"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fileHash");
        elemField.setXmlName(new javax.xml.namespace.QName("http://flvrecord.beans.persistence.app.openmeetings.org/xsd", "fileHash"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fileName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://flvrecord.beans.persistence.app.openmeetings.org/xsd", "fileName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fileSize");
        elemField.setXmlName(new javax.xml.namespace.QName("http://flvrecord.beans.persistence.app.openmeetings.org/xsd", "fileSize"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("flvHeight");
        elemField.setXmlName(new javax.xml.namespace.QName("http://flvrecord.beans.persistence.app.openmeetings.org/xsd", "flvHeight"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("flvRecordingId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://flvrecord.beans.persistence.app.openmeetings.org/xsd", "flvRecordingId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("flvRecordingLog");
        elemField.setXmlName(new javax.xml.namespace.QName("http://flvrecord.beans.persistence.app.openmeetings.org/xsd", "flvRecordingLog"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("flvRecordingMetaData");
        elemField.setXmlName(new javax.xml.namespace.QName("http://flvrecord.beans.persistence.app.openmeetings.org/xsd", "flvRecordingMetaData"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("flvWidth");
        elemField.setXmlName(new javax.xml.namespace.QName("http://flvrecord.beans.persistence.app.openmeetings.org/xsd", "flvWidth"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("height");
        elemField.setXmlName(new javax.xml.namespace.QName("http://flvrecord.beans.persistence.app.openmeetings.org/xsd", "height"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("inserted");
        elemField.setXmlName(new javax.xml.namespace.QName("http://flvrecord.beans.persistence.app.openmeetings.org/xsd", "inserted"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("insertedBy");
        elemField.setXmlName(new javax.xml.namespace.QName("http://flvrecord.beans.persistence.app.openmeetings.org/xsd", "insertedBy"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isFolder");
        elemField.setXmlName(new javax.xml.namespace.QName("http://flvrecord.beans.persistence.app.openmeetings.org/xsd", "isFolder"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isImage");
        elemField.setXmlName(new javax.xml.namespace.QName("http://flvrecord.beans.persistence.app.openmeetings.org/xsd", "isImage"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isInterview");
        elemField.setXmlName(new javax.xml.namespace.QName("http://flvrecord.beans.persistence.app.openmeetings.org/xsd", "isInterview"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isPresentation");
        elemField.setXmlName(new javax.xml.namespace.QName("http://flvrecord.beans.persistence.app.openmeetings.org/xsd", "isPresentation"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isRecording");
        elemField.setXmlName(new javax.xml.namespace.QName("http://flvrecord.beans.persistence.app.openmeetings.org/xsd", "isRecording"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("organization_id");
        elemField.setXmlName(new javax.xml.namespace.QName("http://flvrecord.beans.persistence.app.openmeetings.org/xsd", "organization_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ownerId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://flvrecord.beans.persistence.app.openmeetings.org/xsd", "ownerId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("parentFileExplorerItemId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://flvrecord.beans.persistence.app.openmeetings.org/xsd", "parentFileExplorerItemId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("previewImage");
        elemField.setXmlName(new javax.xml.namespace.QName("http://flvrecord.beans.persistence.app.openmeetings.org/xsd", "previewImage"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("progressPostProcessing");
        elemField.setXmlName(new javax.xml.namespace.QName("http://flvrecord.beans.persistence.app.openmeetings.org/xsd", "progressPostProcessing"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("recordEnd");
        elemField.setXmlName(new javax.xml.namespace.QName("http://flvrecord.beans.persistence.app.openmeetings.org/xsd", "recordEnd"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("recordStart");
        elemField.setXmlName(new javax.xml.namespace.QName("http://flvrecord.beans.persistence.app.openmeetings.org/xsd", "recordStart"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("recorderStreamId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://flvrecord.beans.persistence.app.openmeetings.org/xsd", "recorderStreamId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("room");
        elemField.setXmlName(new javax.xml.namespace.QName("http://flvrecord.beans.persistence.app.openmeetings.org/xsd", "room"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://rooms.beans.persistence.app.openmeetings.org/xsd", "Rooms"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("room_id");
        elemField.setXmlName(new javax.xml.namespace.QName("http://flvrecord.beans.persistence.app.openmeetings.org/xsd", "room_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("updated");
        elemField.setXmlName(new javax.xml.namespace.QName("http://flvrecord.beans.persistence.app.openmeetings.org/xsd", "updated"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("width");
        elemField.setXmlName(new javax.xml.namespace.QName("http://flvrecord.beans.persistence.app.openmeetings.org/xsd", "width"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(java.lang.String mechType, java.lang.Class _javaType, javax.xml.namespace.QName _xmlType) {
        return new org.apache.axis.encoding.ser.BeanSerializer(_javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(java.lang.String mechType, java.lang.Class _javaType, javax.xml.namespace.QName _xmlType) {
        return new org.apache.axis.encoding.ser.BeanDeserializer(_javaType, _xmlType, typeDesc);
    }
}
