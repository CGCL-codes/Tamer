package com.bluesky.jwf.component.captcha;

import java.util.Random;

public class MathCaptchaService implements CaptchaService {

    private static char[] ops = new char[] { '+', '-', '*', '/' };

    private int calc(String exp) {
        String[] ss = exp.split(" ");
        int a = Integer.valueOf(ss[0]);
        int b = Integer.valueOf(ss[2]);
        String op = ss[1];
        char cop = op.charAt(0);
        int c;
        switch(cop) {
            case '+':
                c = a + b;
                break;
            case '-':
                c = a - b;
                break;
            case '*':
                c = a * b;
                break;
            case '/':
                c = a / b;
                break;
            default:
                c = a + b;
        }
        return c;
    }

    @Override
    public boolean check(String answer, String randomString) {
        return (String.valueOf(calc(randomString)).equals(answer));
    }

    @Override
    public String generateRandomString() {
        Random rand = new Random();
        int a = rand.nextInt(10);
        int b = rand.nextInt(10);
        int op = rand.nextInt(3);
        String exp = String.format("%d %c %d", a, ops[op], b);
        return exp;
    }

    /**
	 * the question should be a html segment
	 */
    @Override
    public String getQuestion(String randomString) {
        String s = "<p>Please do the math:" + randomString + "</p>";
        return s;
    }

    public static void main(String[] args) {
        MathCaptchaService svc = new MathCaptchaService();
        String rs = svc.generateRandomString();
        String q = svc.getQuestion(rs);
        int rightAnswer = svc.calc(rs);
        boolean b = svc.check(String.valueOf(rightAnswer), rs);
        System.out.println(q);
        System.out.println(rightAnswer);
        System.out.println(b);
    }
}
