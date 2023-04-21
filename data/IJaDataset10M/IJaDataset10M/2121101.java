package org.frameworkset.spi.assemble;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 
 * <p>Title: ProviderInfoQueue.java</p>
 *
 * <p>Description: ��������ṩ�߶��У������ȼ�˳��
 * ��Ź������Ķ���ṩ��
 * </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>bboss workgroup</p>
 * @Date Sep 8, 2008 10:05:06 AM
 * @author biaoping.yin,���ƽ
 * @version 1.0
 */
public class ProviderInfoQueue implements java.io.Serializable {

    private List list = new ArrayList();

    public static void main(String[] args) {
        ProviderInfoQueue providerqueue = new ProviderInfoQueue();
    }

    public SecurityProviderInfo getSecurityProviderInfo(int i) {
        return (SecurityProviderInfo) list.get(i);
    }

    public void addSecurityProviderInfo(SecurityProviderInfo securityProvider) {
        this.list.add(securityProvider);
        java.util.Collections.sort(list, new java.util.Comparator() {

            /**
        	 * provider�������priorֵԽС�����ȼ�Խ��
        	 */
            public int compare(Object o1, Object o2) {
                SecurityProviderInfo o1_ = (SecurityProviderInfo) o1;
                SecurityProviderInfo o2_ = (SecurityProviderInfo) o2;
                if (o1_.getPrior() < o2_.getPrior()) return 1; else if (o1_.getPrior() > o2_.getPrior()) return -1;
                return 0;
            }
        });
    }

    public int size() {
        return list.size();
    }
}
