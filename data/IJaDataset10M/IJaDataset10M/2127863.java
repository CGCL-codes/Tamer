package com.cvicse.ks.factorymethod;

/**
 * <p>
 * Description: The concrete shape factory.It provide one factory method
 * used to create circle shape.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: CVIC SE
 * </p>
 * 
 * @author geng_lchao
 * @checker geng_lchao
 * @version 1.0
 * @created at 2007-08-08
 * @modified by geng_lchao at at 2007-08-08
 */
public class CircleFactory implements ShapeFactory {

    /**
	 * It's important to return shape abstract type
	 * not square concrete type.������Ƿ���һ�������
	 * ��Ʒ�࣬�����ʵ��ϸ�ڱ�¶����磬ͬ��Ҳ�����ڻ��ڳ���
	 * ���б�̣�
	 */
    public Shape createShape() {
        return new Circle();
    }
}
