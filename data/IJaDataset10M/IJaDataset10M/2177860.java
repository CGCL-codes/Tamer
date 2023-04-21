package nps.job.meter;

import java.util.List;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
import java.math.BigDecimal;

/**
 *  MeterStack
 *    ������λ��ջ�������ض���ʽ�ļ�����λ���ʽ
 *    ������λ���ʽ�ĸ�ʽ��Ҫ�У�
 *      1.����+��λ/��λ������5��/��
 *      2.����+��λ+�����+per+�����+��λ������5 pcs per carton
 *      3.����+��λ+�����+a+�����+��λ������ 5 kg a barrel
 *    ���е�һ����λҲ���Է�����ǰ�棬���� RMB 5 per pcs 
 *    ���������ǣ��ո�tab��ȫ�ǿո񡢻س�����
 *
 *    ���������λ���ʽ�����ʹ�ÿո�tab��ȫ�ǿո񡢶��š��ֺš�ȫ�Ƕ��š�ȫ�Ƿֺš��س�����
 *    ����
 *       5��/��,10��/��,50��/���
 *
 *    ע�����
 *       1.���е�λ����Ҫ�ø���ǰ��һ��
 *       2.������λ�м䲻���ÿո�������λ���ʹ�ü�д
 *       3.��λ�ȶ�����ִ�Сд��
 *       4.�������ظ�����ģ����󵽺�����ȴ�������
 *           ���磬���� "5��/��,10��/��,100��/��,50��/���"��ϵͳ������100��/��
 *       5.ֻ��������+��λ��ϵģ����ܳ������������
 *           ���磬"5��"�ǺϷ��ģ�����"5�� 5Ԫ/��"�ǲ��Ϸ���
 *           ��һ����λҲ���Է�����ǰ�棬����"RMB 5"�ǺϷ���
 *
 *  Copyright (c) 2008

 *  @author jialin
 *  @version 1.0
 */
public class MeterStack {

    private List<Meter> stack = null;

    public MeterStack() {
        stack = new ArrayList();
    }

    public MeterStack(List<Meter> meters) {
        stack = new ArrayList();
        stack.addAll(meters);
    }

    public void Add(String num, String left, String right) {
        Meter meter = new Meter(num, left, right);
        stack.add(meter);
    }

    public void Add(Meter m) {
        stack.add(m);
    }

    public String Validate() {
        MeterStack longest_stack = Sort();
        List<Meter> longest_meterlist = longest_stack.ToList();
        if (longest_meterlist.size() == stack.size()) return null;
        StringBuffer buf = new StringBuffer();
        for (Meter meter : stack) {
            if (!longest_meterlist.contains(meter)) {
                buf.append("Warning:û���õı��ʽ," + meter.num.toString() + meter.left + "/" + meter.right);
                buf.append("\r\n");
            }
        }
        return buf.toString();
    }

    public MeterStack Sort() {
        if (stack.size() <= 1) return this;
        List<Meter> list = new ArrayList();
        for (Meter meter : stack) {
            Vector token_searched = new Vector();
            List<Meter> sort_list = FindLeft(token_searched, meter.right);
            sort_list.add(0, meter);
            if (sort_list.size() > list.size()) {
                list = sort_list;
            }
        }
        MeterStack new_stack = new MeterStack(list);
        return new_stack;
    }

    private List<Meter> FindLeft(Vector token_searched, String token) {
        List<Meter> list = new ArrayList();
        if (token == null) return list;
        if (token_searched.contains(token)) return list;
        for (Meter meter_find : stack) {
            if (meter_find.left.equals(token)) {
                list.add(meter_find);
                token_searched.add(token);
                if (!token.equals(meter_find.right)) list.addAll(FindLeft(token_searched, meter_find.right));
                break;
            }
        }
        return list;
    }

    public BigDecimal GetNum(String left, String right) {
        for (Meter meter : stack) {
            if (right.equals(meter.right)) {
                if (left.equals(meter.left)) {
                    return meter.num;
                } else {
                    BigDecimal ret = GetNum(left, meter.left);
                    if (ret == null) return null;
                    return meter.num.multiply(ret);
                }
            }
        }
        return null;
    }

    public List<Meter> ToList() {
        return stack;
    }

    public Meter GetNth(int n) {
        if (stack.size() == 0) return null;
        return stack.get(n);
    }

    public List<Meter> GetMetersByLeft(String token) {
        List<Meter> list = new ArrayList();
        for (Meter meter : stack) {
            if (token.equals(meter.left)) {
                list.add(meter);
            }
        }
        return list;
    }

    public List<Meter> GetMetersByRight(String token) {
        List<Meter> list = new ArrayList();
        for (Meter meter : stack) {
            if (token.equals(meter.right)) {
                list.add(meter);
            }
        }
        return list;
    }

    public class Meter {

        public BigDecimal num;

        public String left;

        public String right;

        public Meter(String num, String left, String right) {
            this.num = new BigDecimal(num);
            this.left = left;
            this.right = right;
        }
    }
}
