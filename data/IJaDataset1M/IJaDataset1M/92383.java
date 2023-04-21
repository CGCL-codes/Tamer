package com.performance.model;

import java.io.Serializable;
import com.jxva.dao.annotation.OneToOne;
import com.jxva.dao.annotation.Table;

/**
 * 
 * @author  The Jxva Framework Foundation
 * @since   1.0
 * @version 2010-02-10 10:00:22 by Automatic Generate Toolkit
 */
@Table(name = "tbl_color_ring", increment = "colorRingId", primaryKeys = { "colorRingId" })
public class ColorRing implements Serializable {

    private static final long serialVersionUID = 1L;

    private java.lang.Integer colorRingId;

    private java.lang.String partnerCode;

    private java.lang.String operatorCode;

    private java.lang.Integer typeId;

    private java.lang.String artistName;

    private java.lang.String name;

    private java.lang.String artist;

    private java.lang.String auditionUrl;

    private java.lang.String downloadUrl;

    private java.lang.Integer clickNum;

    private java.lang.Integer downloadNum;

    private java.lang.Integer isCommend;

    private java.lang.Integer songId;

    private java.lang.Integer artistId;

    private java.lang.String songName;

    private java.sql.Timestamp addTime;

    private java.lang.String adaptNetwork;

    private java.lang.String operatorRingId;

    private java.sql.Date expiredDate;

    private java.lang.Integer commendSeq;

    @OneToOne(field = "typeId", foreignKey = "typeId")
    private ColorRingType colorRingType;

    public java.lang.Integer getColorRingId() {
        return this.colorRingId;
    }

    public void setColorRingId(java.lang.Integer colorRingId) {
        this.colorRingId = colorRingId;
    }

    public java.lang.String getPartnerCode() {
        return this.partnerCode;
    }

    public void setPartnerCode(java.lang.String partnerCode) {
        this.partnerCode = partnerCode;
    }

    public java.lang.String getOperatorCode() {
        return this.operatorCode;
    }

    public void setOperatorCode(java.lang.String operatorCode) {
        this.operatorCode = operatorCode;
    }

    public java.lang.Integer getTypeId() {
        return this.typeId;
    }

    public void setTypeId(java.lang.Integer typeId) {
        this.typeId = typeId;
    }

    public java.lang.String getArtistName() {
        return this.artistName;
    }

    public void setArtistName(java.lang.String artistName) {
        this.artistName = artistName;
    }

    public java.lang.String getName() {
        return this.name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public java.lang.String getArtist() {
        return this.artist;
    }

    public void setArtist(java.lang.String artist) {
        this.artist = artist;
    }

    public java.lang.String getAuditionUrl() {
        return this.auditionUrl;
    }

    public void setAuditionUrl(java.lang.String auditionUrl) {
        this.auditionUrl = auditionUrl;
    }

    public java.lang.String getDownloadUrl() {
        return this.downloadUrl;
    }

    public void setDownloadUrl(java.lang.String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public java.lang.Integer getClickNum() {
        return this.clickNum;
    }

    public void setClickNum(java.lang.Integer clickNum) {
        this.clickNum = clickNum;
    }

    public java.lang.Integer getDownloadNum() {
        return this.downloadNum;
    }

    public void setDownloadNum(java.lang.Integer downloadNum) {
        this.downloadNum = downloadNum;
    }

    public java.lang.Integer getIsCommend() {
        return this.isCommend;
    }

    public void setIsCommend(java.lang.Integer isCommend) {
        this.isCommend = isCommend;
    }

    public java.lang.Integer getSongId() {
        return this.songId;
    }

    public void setSongId(java.lang.Integer songId) {
        this.songId = songId;
    }

    public java.lang.Integer getArtistId() {
        return this.artistId;
    }

    public void setArtistId(java.lang.Integer artistId) {
        this.artistId = artistId;
    }

    public java.lang.String getSongName() {
        return this.songName;
    }

    public void setSongName(java.lang.String songName) {
        this.songName = songName;
    }

    public java.sql.Timestamp getAddTime() {
        return this.addTime;
    }

    public void setAddTime(java.sql.Timestamp addTime) {
        this.addTime = addTime;
    }

    public java.lang.String getAdaptNetwork() {
        return this.adaptNetwork;
    }

    public void setAdaptNetwork(java.lang.String adaptNetwork) {
        this.adaptNetwork = adaptNetwork;
    }

    public java.lang.String getOperatorRingId() {
        return this.operatorRingId;
    }

    public void setOperatorRingId(java.lang.String operatorRingId) {
        this.operatorRingId = operatorRingId;
    }

    public java.sql.Date getExpiredDate() {
        return this.expiredDate;
    }

    public void setExpiredDate(java.sql.Date expiredDate) {
        this.expiredDate = expiredDate;
    }

    public java.lang.Integer getCommendSeq() {
        return this.commendSeq;
    }

    public void setCommendSeq(java.lang.Integer commendSeq) {
        this.commendSeq = commendSeq;
    }

    public ColorRingType getColorRingType() {
        return colorRingType;
    }

    public void setColorRingType(ColorRingType colorRingType) {
        this.colorRingType = colorRingType;
    }

    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public int hashCode() {
        return super.hashCode();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[ ");
        sb.append("colorRingId=").append(colorRingId).append(',');
        sb.append("partnerCode=").append(partnerCode).append(',');
        sb.append("operatorCode=").append(operatorCode).append(',');
        sb.append("typeId=").append(typeId).append(',');
        sb.append("artistName=").append(artistName).append(',');
        sb.append("name=").append(name).append(',');
        sb.append("artist=").append(artist).append(',');
        sb.append("auditionUrl=").append(auditionUrl).append(',');
        sb.append("downloadUrl=").append(downloadUrl).append(',');
        sb.append("clickNum=").append(clickNum).append(',');
        sb.append("downloadNum=").append(downloadNum).append(',');
        sb.append("isCommend=").append(isCommend).append(',');
        sb.append("songId=").append(songId).append(',');
        sb.append("artistId=").append(artistId).append(',');
        sb.append("songName=").append(songName).append(',');
        sb.append("addTime=").append(addTime).append(',');
        sb.append("adaptNetwork=").append(adaptNetwork).append(',');
        sb.append("operatorRingId=").append(operatorRingId).append(',');
        sb.append("expiredDate=").append(expiredDate).append(',');
        sb.append("commendSeq=").append(commendSeq).append(" ]");
        return sb.toString();
    }
}
