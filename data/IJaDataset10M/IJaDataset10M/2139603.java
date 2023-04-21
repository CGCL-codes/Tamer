package com.xiaxueqi.service;

import java.io.File;

/**
 * 文件上传服务，支持单文件及多文件上传服务。
 * @author liangThink
 */
public interface UploadService {

    public static final int BUFFER_SIZE = 16 * 1024;

    /**
	 * 单文件上传
	 * 
	 * @param uploadFile
	 *            要上传的文件
	 * @param fileName
	 *            上传文件名字
	 * @param savePath
	 *            上传文件存放的路径
	 * @return 文件储存在服务器上的相对路径
	 */
    public String upload(File uploadFile, String fileName, String savePath) throws Exception;

    /**
	 * 多文件上传
	 * 
	 * @param uploadFiles
	 *            要上传的文件数组File[]类型，注意在input标签中所有file类型name属性一样
	 * @param fileNames
	 *            上传文件名字数组
	 * @param savePath
	 *            上传文件存放的路径
	 * @return 文件储存在服务器上的相对路径的数组
	 */
    public String[] upload(File[] uploadFiles, String[] fileNames, String savePath) throws Exception;
}
