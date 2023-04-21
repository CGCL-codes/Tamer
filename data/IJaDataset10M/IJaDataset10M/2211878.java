package com.kongur.network.erp.domain.demo;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author gaojf
 * @version $Id: ItemDemo.java,v 0.1 2012-3-9 ����06:51:29 gaojf Exp $
 */
public class ItemDemo {

    private String name;

    private String content;

    private MultipartFile imgFileMain;

    private MultipartFile imgFile1;

    private MultipartFile imgFile2;

    private MultipartFile imgFile3;

    private MultipartFile imgFile4;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MultipartFile getImgFileMain() {
        return imgFileMain;
    }

    public void setImgFileMain(MultipartFile imgFileMain) {
        this.imgFileMain = imgFileMain;
    }

    public MultipartFile getImgFile1() {
        return imgFile1;
    }

    public void setImgFile1(MultipartFile imgFile1) {
        this.imgFile1 = imgFile1;
    }

    public MultipartFile getImgFile2() {
        return imgFile2;
    }

    public void setImgFile2(MultipartFile imgFile2) {
        this.imgFile2 = imgFile2;
    }

    public MultipartFile getImgFile3() {
        return imgFile3;
    }

    public void setImgFile3(MultipartFile imgFile3) {
        this.imgFile3 = imgFile3;
    }

    public MultipartFile getImgFile4() {
        return imgFile4;
    }

    public void setImgFile4(MultipartFile imgFile4) {
        this.imgFile4 = imgFile4;
    }
}
