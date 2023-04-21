package com.taobao.api.response;

import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.domain.TaohuaRootDirectory;
import com.taobao.api.TaobaoResponse;

/**
 * TOP API: taobao.taohua.directory.get response.
 * 
 * @author auto create
 * @since 1.0, null
 */
public class TaohuaDirectoryGetResponse extends TaobaoResponse {

    private static final long serialVersionUID = 4463426674285262338L;

    /** 
	 * 淘花文档目录
	 */
    @ApiField("tree_vo")
    private TaohuaRootDirectory treeVo;

    public void setTreeVo(TaohuaRootDirectory treeVo) {
        this.treeVo = treeVo;
    }

    public TaohuaRootDirectory getTreeVo() {
        return this.treeVo;
    }
}
