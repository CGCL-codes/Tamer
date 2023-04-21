package jfaceViewer;

public class Entity {

    public String entityName;

    public Entity[] children = new Entity[0];

    public Entity parent;

    public Entity(String entityName) {
        this.entityName = entityName;
    }

    public Entity(String entityName, Entity[] children) {
        this.entityName = entityName;
        this.children = children;
        for (int i = 0; i < children.length; i++) {
            children[i].parent = this;
        }
    }

    public static Entity[] node() {
        return new Entity[] { new Entity("�й�", new Entity[] { new Entity("����", new Entity[] { new Entity("�廪��ѧ"), new Entity("������ѧ") }), new Entity("�Ϻ�", new Entity[] { new Entity("������ѧ"), new Entity("�Ϻ�����") }), new Entity("���", new Entity[] { new Entity("�Ͽ���ѧ"), new Entity("����ѧ") }) }), new Entity("����", new Entity[] { new Entity("����������", new Entity[] { new Entity("�����ѧ"), new Entity("��ʡ�?ѧԺ") }), new Entity("����Ҹ���", new Entity[] { new Entity("Ү³��ѧ") }), new Entity("��������", new Entity[] { new Entity("����˹�ٴ�ѧ") }) }), new Entity("Ӣ��", new Entity[] { new Entity("ţ����", new Entity[] { new Entity("ţ���ѧ") }), new Entity("������", new Entity[] { new Entity("���Ŵ�ѧ") }) }) };
    }
}
