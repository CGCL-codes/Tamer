package org.cmsuite2.model.product;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.cmsuite2.enumeration.MediaType;
import org.cmsuite2.model.AbstractEntity;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "cmsuite_product_media")
public class ProductMedia extends AbstractEntity<ProductMedia> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "i_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "s_mediaType", length = 255, nullable = false)
    private MediaType mediaType;

    @Basic
    @Type(type = "true_false")
    @Column(name = "s_online", nullable = true)
    private boolean online;

    @Column(name = "i_viewOrder", nullable = true)
    private int viewOrder;

    @Column(name = "i_contentType", nullable = true)
    private String contentType;

    @Column(name = "i_fileName", nullable = true)
    private String fileName;

    @Column(name = "i_fileExtension", length = 3, nullable = true)
    private String fileExtension;

    @Lob
    @Column(name = "b_contentData", length = 1073741824)
    private byte[] contentData;

    @DateTimeFormat
    @Column(name = "d_contentModDate", nullable = false)
    private Date contentModDate;

    @ManyToOne
    private Product product;

    @Override
    public long getId() {
        return id;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public int getViewOrder() {
        return viewOrder;
    }

    public void setViewOrder(int viewOrder) {
        this.viewOrder = viewOrder;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public byte[] getContentData() {
        return contentData;
    }

    public void setContentData(byte[] contentData) {
        this.contentData = contentData;
    }

    public Date getContentModDate() {
        return contentModDate;
    }

    public void setContentModDate(Date contentModDate) {
        this.contentModDate = contentModDate;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ProductMedia other = (ProductMedia) obj;
        if (id != other.id) return false;
        return true;
    }

    @Override
    public String toString() {
        return "ProductMedia [id=" + id + ", mediaType=" + mediaType + ", online=" + online + ", viewOrder=" + viewOrder + ", contentType=" + contentType + ", fileName=" + fileName + ", fileExtension=" + fileExtension + ", product=" + product + "]";
    }
}
