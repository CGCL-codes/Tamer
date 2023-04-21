package aoetec.javalang._221variables;

import aoetec.util.lession.Lesson;

@Lesson(title = "变量", keyword = "", lastModifed = "2007/10/30", content = { "Java语言里的变量分为4种类别、两种数据类型", "变量的类别，字段（field）与变量（variable）的区别", "   实例变量（instance varibale）：非静态的字段，一个对象一份，存储各个对象的状态", "   类变量（class varibale）：静态字段，只有一份，类的对象共享", "   局部变量（local varibale）：在方法体和语句块里定义的变量", "   形式参数（parameter）：方法声明里定义的变量", "变量的数据类型", "   基本类型，共8个，byte,short,int,long,char,float,double,boolean", "   引用类型，数组、字符串、类、接口", "变量的作用域", "   实例变量：实例方法（类里定义的非静态方法）、构造方法、非静态代码块", "   类变量：类里定义的所有方法、静态代码块、非静态代码块", "   局部变量：从定义处开始至起所在的方法和代码块结束", "   形式参数：所在的方法体", "局部变量对字段的屏蔽（shadow the field）", "   当局部变量和字段的名称相同时，在局部变量的作用域内，局部变量会屏蔽掉字段", "变量的命名", "   变量名称是大小写敏感的，age与AGE表示两个不同的变量", "   变量名称的长度不受限制", "   变量名称由Unicode字目（如'a'、'中'）、数字、下划线'_'、美元符'$'组成", "   变量名称的第一个字母不能为数字", "   变量名称不能为Java的关键字（keyword）和保留字（reserved word）", "   （约定）由多个单词组成时，第二个及以后的每个单词的首字母大写，其余字母小些", "   （约定）常量的每个字母都大写，单词之间以下划线'_'隔开，如final int NUM_GEARS;", "   （约定）除非在定义常量时，变量名里不使用下划线'_'（但使用也不为语法错误）", "   （约定）变量名称里约定不使用美元符'$'（但使用也不为语法错误）", "类的命名", "   类名的首字母大写，其它同变量的命名", "方法的命名", "   约定情况下方法名的第一个（或仅有的一个）单词应为动词，其它同变量的命名", "包的命名", "   包名里的每个字母都为小些字母，其它同'变量的命名'", "" })
public final class A01_Variable {

    public static void main(String[] args) {
        System.out.println(5.0 / 2);
    }
}

class VariableDemo {

    static String classVar = "class variable";

    String instanceVar = "instance variable";

    {
        int localVar = 0;
    }

    void prt(int parameter) {
        String localVar = "local variable";
    }
}
