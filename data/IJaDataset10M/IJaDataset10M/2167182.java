package com.hy.mydesktop.shared.persistence.domain.gxt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.extjs.gxt.ui.client.core.FastMap;
import com.extjs.gxt.ui.client.core.FastSet;
import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.BeanModelTag;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.hy.mydesktop.shared.persistence.domain.gxt.constant.GxtMetaModelTypeEnum;

public class GxtComponentMetaModel extends BaseModel {

    /**
	 * 嵌套集合为结构，存放的树形结构：以先序遍历排序内部元素
	 */
    private List<GxtComponentMetaNodeModel> list = new ArrayList<GxtComponentMetaNodeModel>();

    public List<GxtComponentMetaNodeModel> getList() {
        return list;
    }

    public void setList(List<GxtComponentMetaNodeModel> list) {
        this.list = list;
    }

    public int indexOf(GxtComponentMetaNodeModel gxtComponentMetaNodeModel) {
        return list.indexOf(gxtComponentMetaNodeModel);
    }

    /**
	 * 
	 * <ul>
	 * <li>方法含义：基于嵌套集合（先序遍历），得到当期节点在先序遍历方式洗的下一个节点</li>
	 * <li>方法作者：花宏宇</li>
	 * <li>编写日期：2010-8-29；时间：下午下午05:35:41</li>
	 * </ul>
	 * <ul>
	 * <b>修订编号：</b>
	 * <li>修订日期：</li>
	 * <li>修订作者：</li>
	 * <li>修订原因：</li>
	 * <li>修订内容：</li>
	 * </ul>
	 * @param gxtComponentMetaNodeModel
	 * @return
	 */
    public GxtComponentMetaNodeModel getNextNode(GxtComponentMetaNodeModel gxtComponentMetaNodeModel) {
        int index = list.indexOf(gxtComponentMetaNodeModel);
        if (index < list.size() - 1) {
            return list.get(index + 1);
        } else {
            return null;
        }
    }

    public int getChildCount(GxtComponentMetaNodeModel gxtComponentMetaNodeModel) {
        return this.getChildren(gxtComponentMetaNodeModel).size();
    }

    /**
	 * 
	 * <ul>
	 * <li>方法含义：获得当前节点的所有直接子节点（通过算法，可以直接计算出子节点，而不用遍历list）</li>
	 * <li>方法作者：花宏宇</li>
	 * <li>编写日期：2010-8-29；时间：下午上午10:24:35</li>
	 * </ul>
	 * <ul>
	 * <b>修订编号：</b>
	 * <li>修订日期：</li>
	 * <li>修订作者：</li>
	 * <li>修订原因：</li>
	 * <li>修订内容：</li>
	 * </ul>
	 * @param gxtComponentMetaNodeModel
	 * @return
	 */
    public List<GxtComponentMetaNodeModel> getChildren(GxtComponentMetaNodeModel gxtComponentMetaNodeModel) {
        return null;
    }

    public GxtComponentMetaNodeModel getParent(GxtComponentMetaNodeModel gxtComponentMetaNodeModel) {
        GxtComponentMetaNodeModel tempNodeModel = null;
        for (GxtComponentMetaNodeModel node : list) {
            if (gxtComponentMetaNodeModel.getLeftValue() > node.getLeftValue() && gxtComponentMetaNodeModel.getLeftValue() < node.getRightValue()) {
                tempNodeModel = node;
            }
        }
        return tempNodeModel;
    }

    /**
	 * 
	 * <ul>
	 * <li>方法含义：获取树形结构（嵌套集合）的根节点</li>
	 * <li>方法作者：花宏宇</li>
	 * <li>编写日期：2010-8-29；时间：下午上午09:50:44</li>
	 * </ul>
	 * <ul>
	 * <b>修订编号：</b>
	 * <li>修订日期：</li>
	 * <li>修订作者：</li>
	 * <li>修订原因：</li>
	 * <li>修订内容：</li>
	 * </ul>
	 * @return
	 */
    public GxtComponentMetaNodeModel getRoot() {
        return list.get(0);
    }

    /**
	 * 
	 * <ul>
	 * <li>方法含义：判断当前节点是否为叶子节点</li>
	 * <li>方法作者：花宏宇</li>
	 * <li>编写日期：2010-8-30；时间：下午上午10:15:55</li>
	 * </ul>
	 * <ul>
	 * <b>修订编号：</b>
	 * <li>修订日期：</li>
	 * <li>修订作者：</li>
	 * <li>修订原因：</li>
	 * <li>修订内容：</li>
	 * </ul>
	 * @param gxtComponentMetaNodeModel
	 * @return
	 */
    public boolean isLeaf(GxtComponentMetaNodeModel gxtComponentMetaNodeModel) {
        if (gxtComponentMetaNodeModel.getLeftValue() + 1 == gxtComponentMetaNodeModel.getRightValue()) {
            return true;
        } else {
            return false;
        }
    }

    /**
	 * 
	 * <ul>
	 * <li>方法含义：GxtComponentMetaModel的数据类型：普通控件或控件的嵌套集合</li>
	 * <li>方法作者：花宏宇</li>
	 * <li>编写日期：2010-8-29；时间：下午上午09:40:46</li>
	 * </ul>
	 * <ul>
	 * <b>修订编号：</b>
	 * <li>修订日期：</li>
	 * <li>修订作者：</li>
	 * <li>修订原因：</li>
	 * <li>修订内容：</li>
	 * </ul>
	 * @return
	 */
    public GxtMetaModelTypeEnum getGxtMetaModelTypeEnum() {
        if (list.size() == 1) {
            return GxtMetaModelTypeEnum.BASE_GXT_META_MODEL_TYPE;
        } else {
            return GxtMetaModelTypeEnum.TREE_GXT_META_MODEL_TYPE;
        }
    }

    public GxtMetaModelTypeEnum getGxtControllerMetaModelTypeEnum() {
        if (list.size() == 1) {
            return GxtMetaModelTypeEnum.BASE_GXT_CONTROLLER_META_MODEL_TYPE;
        } else {
            return GxtMetaModelTypeEnum.TREE_GXT_CONTROLLER_META_MODEL_TYPE;
        }
    }

    /**
	 * 构造函数
	 */
    public GxtComponentMetaModel() {
        super();
    }

    /**
	 * 构造函数
	 */
    public GxtComponentMetaModel(List<GxtComponentMetaNodeModel> list) {
        super();
        this.setList(list);
    }

    protected void initList() {
    }
}
