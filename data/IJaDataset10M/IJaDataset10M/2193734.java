package com.coderdream.chapter14.chainofresponsibility.sample;

public abstract class Support {

    /**
	 * 问题解决者的名称
	 */
    private String name;

    /**
	 * 转送位置
	 */
    private Support next;

    /**
	 * 产生问题解决者
	 */
    public Support(String name) {
        this.name = name;
    }

    /**
	 * @param next
	 * @return
	 */
    public Support setNext(Support next) {
        this.next = next;
        return next;
    }

    /**
	 * <pre>
	 * 解决问题的步骤
	 * 1、自己能處理，則自己處理；
	 * 2、自己不能處理:
	 * 	a)：后面還有人，則交個下一個人；
	 * 	b)：后面沒有人，則該問題不能處理。
	 * </pre>
	 * 
	 * @param trouble
	 */
    public final void support(Trouble trouble) {
        if (resolve(trouble)) {
            done(trouble);
        } else if (next != null) {
            next.support(trouble);
        } else {
            fail(trouble);
        }
    }

    /**
	 * 解决问题的方法
	 * 
	 * @param trouble
	 * @return
	 */
    protected abstract boolean resolve(Trouble trouble);

    /**
	 * 已解决
	 * 
	 * @param trouble
	 */
    protected void done(Trouble trouble) {
        System.out.println(trouble + " is resolved by " + this + ".");
    }

    /**
	 * 尚未解决
	 * 
	 * @param trouble
	 */
    protected void fail(Trouble trouble) {
        System.out.println(trouble + " cannot be resolved. ");
    }

    public String toString() {
        return "[" + name + "]";
    }
}
